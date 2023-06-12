package web;

import manager.QuizManager;
import manager.UserManager;
import utility.Pair;
import web.path.*; // can we use * here?

import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import struct.QuizProgress;

/**
 * The Server class, responsible for managing client and database connections.
 *
 * @author Ricky Qin, Avery Chan, and Sherwin Okhowat
 */
public class Server {

    private UserManager userManager;

    private QuizManager quizManager;

    private ServerSocket serverSock;

    /**
     * Maps each Pair(String username, Integer id) to a QuizProgress
     */
    private HashMap<Pair<String, Integer>, QuizProgress> quizzes;

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
     * @param dbName The database name (must include {@code .db} file extension)
     */
    public void start(String dbName) {
        userManager = new UserManager(dbName);
        quizManager = new QuizManager(dbName, userManager);
        userManager.connectToDatabase();
        quizManager.connectToDatabase();
        userManager.initialize();// must be initialized before quizManager
        quizManager.initialize();
        cookies = new HashMap<>();
        quizzes = new HashMap<>();

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
     * @param username The username
     * @param password The password
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
     * @return A Pair containing the user's username and password or {@code null} if the
     * session ID doesn't exist or is invalid
     */
    public Pair<String, String> checkSessionID(HTTPRequest request) {
        String cookieFieldValue = request.getField("Cookie");
        if(cookieFieldValue == null) {
            return null;
        }
        String sessionID = cookieFieldValue.substring(cookieFieldValue.indexOf("=")+1);
        return cookies.get(sessionID);
    }

    /**
     * Removes the provided session ID from the list of valid ids, thus invalidating it.
     *
     * @param request The request whose session id will be invalidated.
     */
    public void deleteSessionID(HTTPRequest request) {
        String cookieFieldValue = request.getField("Cookie");
        if(cookieFieldValue != null) {
            String sessionID = cookieFieldValue.substring(cookieFieldValue.indexOf("=")+1);
            cookies.remove(sessionID);
        }
    }


    // --------------- Following methods are for quizzes in session  ---------------

    /**
     * Starts a session for an active quiz for a specific user
     *
     * @param username the username
     * @param id The ID of the quiz
     */
    public void startQuiz(String username, int id) {
        this.quizzes.put(new Pair<>(username, id), new QuizProgress(id, this));
    }

    /**
     * Retrieves a User's progress on a Quiz
     *
     * @param username the username
     * @param id The ID of the quiz
     * @return the QuizProgress {@code null} if there is no active quiz
     */
    public QuizProgress getQuizProgress(String username, int id) {
        return this.quizzes.getOrDefault(new Pair<>(username, id), null);
    }

    /**
     * Ends the ongoing session for an active quiz for a specific user
     *
     * @param username the username
     * @param id The name of the quiz
     */
    public void endQuiz(String username, int id) {
        this.quizzes.remove(new Pair<>(username, id));
    }

/**
 * Inner class - thread for client connection
 *
 * @author Sherwin Okhowat, Avery Chan, and Ricky Qin
 */
  private class ConnectionHandler implements Runnable {

    private DataOutputStream output;// assign printwriter to network stream
    private BufferedReader input;// Stream for network input
    private Socket client;// keeps track of the client socket
    private Server server;// the running server

    /**
     * Constructs a ConnectionHandler
     *
     * @param sock   the socket belonging to this client connection
     * @param server The running server
     */
    ConnectionHandler(Socket sock, Server server) {
        this.client = sock;
        this.server = server;

        // assign all connections to client
        try {
            this.output = new DataOutputStream(client.getOutputStream());
            InputStreamReader stream = new InputStreamReader(client.getInputStream());
            this.input = new BufferedReader(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executed on start of thread
     */
    @Override
    public void run() {
        // Get a message from the client
        ArrayList<String> request = new ArrayList<>();

        // Get a message from the client, loops until a message is received
        try {
            // check for incoming responses
            int contentLength = 0;
            while (input.ready()) {
                String line = input.readLine();
                request.add(line);
                if (line.startsWith("Content-Length:")) {// get the length of the body content, if it exists
                    contentLength = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
                } else if (line.equals("")) {// indicates end of header
                    if (contentLength > 0) {
                        // we must read the body byte by byte, since it may not end in a newline
                        char[] chars = new char[contentLength];
                        input.read(chars, 0, contentLength);
                        request.add(new String(chars));
                    }
                    break;
                }
            }

            if (request.size() != 0) {
                // process request here
                System.out.println("[" + Thread.currentThread() + "] received Request:");
                System.out.println(request);
                System.out.println();
                HTTPRequest requestObj = new HTTPRequest(request);
                processRequest(requestObj).writeResponse(output);
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
     * Processes a request and sends a response to the client
     *
     * @param request The request, split by the line separator
     * @return The response
     */
    private HTTPResponse processRequest(HTTPRequest request) {
        if (request.getType().equals("GET")) {
            String path = request.getPathWithoutQueryString();
            switch (path) {
                case "/":
                    return new RootPage().processRequest(request, server);
                case "/login":
                    return new LoginPage().processRequest(request, server);
                case "/signup":
                    return new SignUpPage().processRequest(request, server);
                case "/signout":
                    return new SignOut().processRequest(request, server);
                case "/home":
                    return new HomePage().processRequest(request, server);
                case "/create-quiz":
                    return new CreateQuizPage().processRequest(request, server);
                case "/account-settings":
                    return new AccountSettingsPage().processRequest(request, server);
                default: {
                    if (path.startsWith("/images/")) {
                        return new FilePath().processRequest(request, server);
                    } else if (path.startsWith("/js/")) {
                        return new FilePath().processRequest(request, server);
                    } else if (path.startsWith("/quiz/")) {
                        if (path.endsWith("/next-question")) {
                            return new QuizGetNextQuestion().processRequest(request, server);
                        } else if (path.endsWith("/start")) {
                            return new StartQuiz().processRequest(request, server);
                        } else {
                            return new ViewQuizPage().processRequest(request, server);
                        }
                    }
                    return new HTTPResponse().setStatus(404);
                }
            }
        } else if (request.getType().equals("POST")) {
            String path = request.getPathWithoutQueryString();
            switch (path) {
                case "/login/submit":
                    return new LoginSubmit().processRequest(request, server);
                case "/signup/submit":
                    return new SignUpSubmit().processRequest(request, server);
                case "/create-quiz/submit":
                    return new CreateQuizSubmit().processRequest(request, server);
                case "/account-settings/change-username-submit":
                    return new ChangeUsernameSubmit().processRequest(request, server);
                case "/account-settings/change-password-submit":
                    return new ChangePasswordSubmit().processRequest(request, server);
                default: {
                    if (path.startsWith("/quiz/") && path.endsWith("/check-answer")) {
                        return new QuizCheckUserAnswer().processRequest(request, server);
                    } else {
                        return new HTTPResponse().setStatus(400);
                    }
                }
            }
        } else {
            return new HTTPResponse().setStatus(501);
        }
    }
}
}