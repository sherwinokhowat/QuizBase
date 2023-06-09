package web;

import manager.QuizManager;
import manager.UserManager;
import struct.Quiz;
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
            Response response = new Response().setStatus(200)
                    // default content type is text/html
                    .setHeaderField("Content-Type", contentType("html"));

            if(request.getType().equals("GET")) {
                System.out.println("["+Thread.currentThread()+"] "+request);

                String path = request.getPathWithoutQueryString();

                if(path.equals("/")) {// homepage

                    WebPage webPage = new WebPage().appendBodyComponents(
                        new Hyperlink("/login", "Log in", true),
                        WebPage.BR_TAG,
                        new Hyperlink("/signup", "Sign up", true));
                    response.appendBody(webPage.toHTMLString());

                } else if(path.equals("/login")) {// login page
                    response.appendBody(new LoginPage().toHTMLString());

                } else if(path.equals("/signup")) {// signup page
                    response.appendBody(new SignUpPage().toHTMLString());

                } else if(path.equals("/home")) {
                    String cookieFieldValue = request.getField("Cookie");
                    String cookie = cookieFieldValue.substring(cookieFieldValue.indexOf("=")+1);
                    Pair<String, String> credentials = checkSessionID(cookie);
                    User user = userManager.authenticateUser(credentials.first(), credentials.second());

                    String query = request.getQueryString();
                    HomePage homepage = new HomePage(user, false);
                    if("quizzes=my".equals(query)) { // display user's quizzes
                        ArrayList<? extends Object> quizzes = quizManager.getUserCreatedQuizzes(user);
                        for(Object quiz : quizzes) {
                            homepage.appendBodyComponents(((Quiz) quiz).toHTMLString());
                        }
                    } else { // display all quizzes
                        ArrayList<? extends Object> quizzes = quizManager.getAllCreatedQuizzes();
                        for(Object quiz : quizzes) {
                            homepage.appendBodyComponents(((Quiz) quiz).toHTMLString());
                        }
                    }
                    response.appendBody(homepage.toHTMLString());

                } else if(path.startsWith("/images/")) {// any path that references stuff inside the images directory
                    byte[] d = null; // byte array for images
                    File imgFile = new File(System.getProperty("user.dir"), path);
                    BufferedInputStream in = null;
                    try {
                        System.out.println("Opening file: " + imgFile.getCanonicalPath());
                        in = new BufferedInputStream(new FileInputStream(imgFile));

                        System.out.println("File Size: " +imgFile.length());
                        d = new byte[(int)imgFile.length()];
                        in.read(d,0,(int)imgFile.length());
                        response.appendBody(d);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    response.setStatus(404);
                }

            } else if(request.getType().equals("POST")) {
                System.out.println("["+Thread.currentThread()+"] "+request);

                if(request.getPath().equals("/login/submit")) {
                    String username = request.getPostBody("username");
                    String password = request.getPostBody("password");
                    User user = userManager.authenticateUser(username, password);

                    WebPage webPage = new WebPage();
                    if(user == null) {
                        webPage.appendBodyComponents("Invalid credentials!");
                    } else {
                        webPage.appendBodyComponents("Logged in!", WebPage.BR_TAG, user.toString(),
                                WebPage.BR_TAG, new Hyperlink("../../home", "Continue", true));
                    }
                    response.appendBody(webPage.toHTMLString());
                    response.setHeaderField("Set-Cookie", "sessionId="+createSessionID(username, password)+"; Path=/");

                } else if(request.getPath().equals("/signup/submit")) {
                    String username = request.getPostBody("username");
                    String password = request.getPostBody("password");
                    User user = userManager.registerUser(username, password);

                    WebPage webPage = new WebPage();
                    if(user == null) {
                        webPage.appendBodyComponents("Unable to sign up! This may be because your username has already been taken, credentials are invalid or a network error occurred.");
                    } else {
                        webPage.appendBodyComponents("Sign up successful!", WebPage.BR_TAG,
                                new Hyperlink("../../login", "Log in", true));
                    }
                    response.appendBody(webPage.toHTMLString());

                }
            } else {
                response.setStatus(400);
            }
            response.writeResponse(output);
        }
    }
}
