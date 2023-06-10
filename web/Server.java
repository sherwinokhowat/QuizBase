package web;

import manager.QuizManager;
import manager.UserManager;
import utility.Pair;
import web.path.HomePage;
import web.path.ImagePath;
import web.path.LoginPage;
import web.path.LoginSubmit;
import web.path.RootPage;
import web.path.SignUpPage;
import web.path.SignUpSubmit;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
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

            while(true) {// always accept connections
                client = serverSock.accept();// wait for connection

                // Note: you might want to keep references to all clients if you plan to broadcast messages
                // Also: Queues are good tools to buffer incoming/outgoing messages

                // create a thread for the new client and pass in the socket
                Thread t = new Thread(new ConnectionHandler(client, this));
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

    /**
     * Checks if the request contains a valid session ID
     *
     * @param request The request
     * @return A Pair containing the user's username and password or {@code null} if the session ID
     * doesn't exist or is invalid
     */
    public Pair<String, String> checkSessionID(HTTPRequest request) {
        String cookieFieldValue = request.getField("Cookie");
        if(cookieFieldValue == null) {
            return null;
        }
        String sessionId = cookieFieldValue.substring(cookieFieldValue.indexOf("=")+1);
        return cookies.get(sessionId);
    }

    /**
     * Removes the provided session ID from the list of valid, thus invalidating it.
     *
     * @param id The session id to invalidate.
     */
    public void deleteSessionId(String id) {
        cookies.remove(id);
    }

}

/**
 * Inner class - thread for client connection
 */
class ConnectionHandler implements Runnable {

    private DataOutputStream output;// assign printwriter to network stream
    private BufferedReader input;// Stream for network input
    private Socket client;// keeps track of the client socket
    private Server server;// the running server

    /**
     * Constructs a ConnectionHandler
     *
     * @param sock the socket belonging to this client connection
     */
    ConnectionHandler(Socket sock, Server server) {
        this.client = sock;
        this.server = server;

        // assign all connections to client
        try {
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
                System.out.println("["+Thread.currentThread()+"] ");
                System.out.println(request);
                HTTPRequest requestObj = new HTTPRequest(request);
                processRequest(requestObj).writeResponse(output);
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
     *
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
     * Processes a request and sends a response to the client
     *
     * @param request The request, split by the line separator
     * @return The response
     */
    private HTTPResponse processRequest(HTTPRequest request) {
        if(request.getType().equals("GET")) {
            String path = request.getPathWithoutQueryString();
            if(path.equals("/")) {// homepage
                return new RootPage().processRequest(request, server);
            } else if(path.equals("/login")) {// login page
                return new LoginPage().processRequest(request, server);
            } else if(path.equals("/signup")) {// signup page
                return new SignUpPage().processRequest(request, server);
            } else if(path.equals("/home")) {// home page (after logging in)
                return new HomePage(false).processRequest(request, server);
            } else if(path.startsWith("/images/")) {// any path that requests an image
                return new ImagePath().processRequest(request, server);
            } else {
                return new HTTPResponse().setStatus(404);
            }

        } else if(request.getType().equals("POST")) {
            if(request.getPath().equals("/login/submit")) {// submit login info
                return new LoginSubmit().processRequest(request, server);
            } else if(request.getPath().equals("/signup/submit")) {// submit signup info
                return new SignUpSubmit().processRequest(request, server);
            } else {
                return new HTTPResponse().setStatus(400);
            }
        } else {
            return new HTTPResponse().setStatus(400);
        }
    }
}