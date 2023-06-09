package web;

import manager.QuizManager;
import manager.UserManager;
import struct.User;
import utility.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Server class, responsible for managing client and database connections.
 *
 * @author Ricky Qin, Avery Chan
 */
public class Server {

    private UserManager userManager;

    private QuizManager quizManager;

    private ServerSocket serverSock;

    private static boolean accepting = true;

    /**
     * Each cookie is mapped to the user's username and password.
     * The cookie will last for the duration of the browser session or while the server is running.
     */
    private HashMap<String, Pair<String, String>> cookies;

    public Server() {
    }

    /**
     * Starts this server.
     *
     * @param port The port number
     * @param dbName The database name (must include {@code .db} file extension)
     */
    public void start(int port, String dbName) {
        userManager = new UserManager(dbName);
        quizManager = new QuizManager(dbName);
        userManager.connectToDatabase();
        quizManager.connectToDatabase();
        userManager.initialize();// must be initialized before quizManager
        quizManager.initialize();
        cookies = new HashMap<>();

        // hold the client connection
        Socket client = null;

        try {
            serverSock = new ServerSocket(5000);// assigns an port to the server
            serverSock.setSoTimeout(0);// will never timeout while waiting for connections
            System.out.println("Accepting Connections");

            while(accepting) {
                client = serverSock.accept();// wait for connection

                // Note: you might want to keep references to all clients if you plan to broadcast messages
                // Also: Queues are good tools to buffer incoming/outgoing messages

                Thread t = new Thread(new ConnectionHandler(client));// create a thread for the new client and pass in the socket
                t.start(); //start the new thread

            }
        } catch(Exception e) {
            System.out.println("Error accepting connection");
            // close all and quit
            try {
                client.close();
            } catch (Exception e1) {
                System.out.println("Failed to close socket");
            }
            System.exit(-1);
        }
    }

    /**
     * Gets this UserManager
     *
     * @return The UserManager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Gets this QuizManager
     *
     * @return The QuizManager
     */
    public QuizManager getQuizManager() {
        return quizManager;
    }

    /**
     * Validates the provided id
     *
     * @param id The session id
     * @return The user's username and password
     */
    public Pair<String, String> checkSessionID(String id) {
        return cookies.get(id);
    }

    /**
     * Generates a unique 20 character session ID, containing AZaz09 characters.
     * Upon generation, it is stored as a cookie.
     *
     * @return The ID
     */
    public String createSessionID(String username, String password) {
        char[] chars = new char[20];
        String id = null;
        do {
            for(int i = 0; i < 20; i++) {
                int randInt = (int)(Math.random()*62);
                if(randInt < 10) {
                    chars[i] = (char)('0'+randInt);
                } else if(randInt < 36) {
                    chars[i] = (char)('A'+randInt-10);
                } else {
                    chars[i] = (char)('a'+randInt-36);
                }
            }
            id = new String(chars);
        } while(cookies.containsKey(id));
        cookies.put(id, new Pair<>(username, password));
        return id;
    }

    //***** Inner class - thread for client connection
    class ConnectionHandler implements Runnable {

        private DataOutputStream output;// assign printwriter to network stream
        private BufferedReader input;// Stream for network input
        private Socket client;// keeps track of the client socket

        /**
         * Constructs a ConnectionHandler
         *
         * @param sock the socket belonging to this client connection
         */
        ConnectionHandler(Socket sock) {
            this.client = sock;// constructor assigns client to this
            try {// assign all connections to client
                this.output = new DataOutputStream(client.getOutputStream());
                InputStreamReader stream = new InputStreamReader(client.getInputStream());
                this.input = new BufferedReader(stream);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Executed on start of thread
         */
        @Override
        public void run() {
            System.out.println("["+Thread.currentThread()+"] "+"this thread just started");
            // Get a message from the client
            ArrayList<String> request = new ArrayList<>();

            // Get a message from the client, loops until a message is received
            try {
                // check for incoming responses
                int contentLength = 0;
                while(input.ready()) {
                    String line = input.readLine();
                    request.add(line);
                    if(line.startsWith("Content-Length:")) {// get the length of the body content, if it exists
                        contentLength = Integer.parseInt(line.substring(line.indexOf(" ")+1));
                    } else if(line.equals("")) {// indicates end of header
                        if(contentLength > 0) {
                            // we must read the body byte by byte, since it may not end in a newline
                            char[] chars = new char[contentLength];
                            input.read(chars, 0, contentLength);
                            request.add(new String(chars));
                        }
                        break;
                    }
                }

                if(request.size() != 0) {
                    // process request here
                    System.out.println("["+Thread.currentThread()+"] "+request.get(0));
                    Request requestObj = new Request(request);
                    processRequest(requestObj);
                    System.out.println("["+Thread.currentThread()+"] "+"processed request");
                }

                input.close();
                output.close();
                client.close();

            } catch (IOException e) {
                System.out.println("Failed to receive response from the client");
                e.printStackTrace();
            }
        }

        /**
         * Gets the extension of a file path (e.g. "/", "/style.css")
         * @param filePath the file path
         * @return the extension (e.g. "html", "css")
         */
        public String getExtension (String filePath) {
            if (filePath.charAt(filePath.length()-1) == '/') {
                filePath += "index.html";
            }
            String[] pathArr = filePath.split("[.]");
            if (pathArr.length <= 1) {
                return "";
            }
            return pathArr[pathArr.length-1];
        }

        /**
         * Returns the MIME type of a given file extension.
         * @param extension The extension (e.g. html, css). Doesn't contain the period before the extension.
         * @return a string representing the MIME type of the extension, or "invalid" if it can't find the type.
         * */
        public String contentType(String extension) {
            String result;
            switch (extension) {
                case "html":
                    result = "text/html";
                    break;
                case "css":
                    result = "text/css";// could look into CSS support for.
                    break;
                case "js":
                    result = "application/javascript";
                    break;
                case "png":
                    result = "image/png";
                    break;
                case "jpg":
                case "jpeg":
                    result = "image/jpeg";
                    break;
                case "gif":
                    result = "image/gif";
                    break;
                default:
                    result = "invalid";
                    break;
            }
            return result;
        }

        /**
         * Processes a request and sends a response to the client
         *
         * @param request The request, split by the line separator
         */
        private void processRequest(Request request) {
            System.out.println(request);
            if(request.getRequestType().equals("GET")) {
                System.out.println("["+Thread.currentThread()+"] "+request);

                // resource path is stored in firstLine[1]
                String path = request.removeQueryString(request.getFileName());

                if(path.equals("/favicon.ico")) {
                    try {
                        output.writeBytes("HTTP/1.1 404 Not Found");
                        output.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                StringBuilder content = new StringBuilder();

                if(path.equals("/")) {// homepage
                    WebPage webPage = new WebPage().appendBodyComponents(
                        new Hyperlink("/login/", "Log in", true),
                        WebPage.BR_TAG,
                        new Hyperlink("/signup/", "Sign up", true));
                    content.append(webPage.toHTMLString());

                } else if(path.equals("/login/")) {// login page
                    LoginPage loginPage = new LoginPage();
                    content.append(loginPage.toHTMLString());
                } else if(path.equals("/signup/")) {// signup page
                    SignUpPage signUp = new SignUpPage();
                    content.append(signUp.toHTMLString());

                } else if(path.equals("/home")) {
                     String query = request.getQueryString();
                     User user = userManager.authenticateUser("sok", "Sok123");
                     HomePage homepage = new HomePage(user);
//                     if(query.equals("quizzes=my")) {
//                         // List<Quiz> list = get the quizzes from the quizmanager for the current user
                    // loop through the list and do content.append(the iterated quiz)
//                     } else if(query.equals("quizzes=all")) {
//  // List<Quiz> list = get the quizzes from the quizmanager for all the users
//                    // loop through the list and do content.append(the iterated quiz)
//                     }

                     content.append(homepage.toHTMLString());
                } else if(path.startsWith("/images/")) {
                    File imgFile = new File(System.getProperty("user.dir"), path);
                    BufferedInputStream in = null;
                    try {
                        System.out.println("Opening file: " + imgFile.getCanonicalPath());
                        in = new BufferedInputStream(new FileInputStream(imgFile));
                        int data;

                        System.out.println("File Size: " +imgFile.length());
                        byte[] d = new byte[(int)imgFile.length()];
                        // need to send this byte array over here.
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        output.writeBytes("HTTP/1.1 404 Not Found");
                        output.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                sendResponse(content.toString(), "Content-Type: "+contentType("html"));
            } else if(request.getRequestType().equals("POST")) {
                System.out.println("["+Thread.currentThread()+"] "+request);

                HashMap<String, String> entries = request.returnPostData();

                if(request.getFileName().equals("/login/submit")) {
                    String username = entries.get("username");
                    String password = entries.get("password");
                    User user = userManager.authenticateUser(username, password);

                    StringBuilder content = new StringBuilder();
                    WebPage webPage = new WebPage();
                    if(user == null) {
                        webPage.appendBodyComponents("Invalid credentials!");
                    } else {
                        webPage.appendBodyComponents("Logged in!", WebPage.BR_TAG, user.toString(),
                                WebPage.BR_TAG, new Hyperlink("../../home/", "Continue", true));
                    }
                    content.append(webPage.toHTMLString());
                    sendResponse(content.toString(), "Content-Type: "+contentType("html"),
                            "Set-Cookie: sessionId="+createSessionID(username, password)+"; Path=../../");

                } else if(request.getFileName().equals("/signup/submit")) {
                    User user = userManager.registerUser(entries.get("username"), entries.get("password"));

                    StringBuilder content = new StringBuilder();
                    WebPage webPage = new WebPage();
                    if(user == null) {
                        webPage.appendBodyComponents("Unable to sign up! This may be because your username has already been taken, credentials are invalid or a network error occurred.");
                    } else {
                        webPage.appendBodyComponents("Sign up successful!", WebPage.BR_TAG,
                                new Hyperlink("../../login/", "Log in", true));
                }
                    content.append(webPage.toHTMLString());
                    sendResponse(content.toString(), "Content-Type: "+contentType("html"));

                }
            } else {
                try {
                    output.writeBytes("HTTP/1.1 400 Bad Request");
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Sends a HTTP 200 (OK) response to the client
         *
         * @param content The body (empty String indicates an empty body)
         * @param headerFields Some number of strings representing the header fields.
         * Note that The "Content-Length" field is always generated, whether or not it is passed in.
         */
        private void sendResponse(String content, String... headerFields) {
            try {
                output.writeBytes("HTTP/1.1 200 OK" + "\n");
                for(String field: headerFields) {
                    output.writeBytes(field + "\n");
                }
                // keep content type as text/html for now, not enough time to support CSS / JS.
                output.writeBytes("Content-Length: " + content.length() + "\n");
                output.writeBytes("\n");
                output.writeBytes(content);
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendByteRequest(byte[] byteArray, String extension) {
            try {
                output.writeBytes("HTTP/1.1 200 OK" + "\n");
                output.writeBytes("Content-Type: " + contentType(extension) + "\n"); // keep it as text/html for now, not enough time to support CSS / JS.
                output.writeBytes("Content-Length: " + byteArray.length + "\n"); // 1 byte = 1 character
                output.writeBytes("\n");
                output.write(byteArray); // we already defined the output stream so this is probably not going to throw an exception.
                output.flush();
            } catch (IOException e) {
                System.out.println("Error created when getting socket output stream - sendByteRequest()");
                e.printStackTrace();
            }

        }

    }
}
