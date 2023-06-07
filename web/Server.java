package web;

import manager.QuizManager;
import manager.UserManager;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

        // hold the client connection
        Socket client = null;

        try {
            serverSock = new ServerSocket(5000);// assigns an port to the server
            serverSock.setSoTimeout(0);// will never timeout while waiting for connections

            while(accepting) {
                System.out.println("["+Thread.currentThread()+"] "+"waiting for connection");
                client = serverSock.accept();// wait for connection
                System.out.println("["+Thread.currentThread()+"] "+"Client connected");

                // Note: you might want to keep references to all clients if you plan to broadcast messages
                // Also: Queues are good tools to buffer incoming/outgoing messages

                Thread t = new Thread(new ConnectionHandler(client));// create a thread for the new client and pass in the socket
                System.out.println("["+Thread.currentThread()+"] "+" about to start new thread");
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

    //***** Inner class - thread for client connection
    class ConnectionHandler implements Runnable {

        private PrintWriter output;// assign printwriter to network stream
        private BufferedReader input;// Stream for network input
        private Socket client;// keeps track of the client socket
        private boolean running;

        /**
         * Constructs a ConnectionHandler
         *
         * @param sock the socket belonging to this client connection
         */
        ConnectionHandler(Socket sock) {
            this.client = sock;// constructor assigns client to this
            try {// assign all connections to client
                this.output = new PrintWriter(client.getOutputStream());
                InputStreamReader stream = new InputStreamReader(client.getInputStream());
                this.input = new BufferedReader(stream);
            } catch(IOException e) {
                e.printStackTrace();
            }
            running = true;
        }

        /**
         * Executed on start of thread
         */
        @Override
        public void run() {
            System.out.println("["+Thread.currentThread()+"] "+" this thread just started");
            // Get a message from the client
            ArrayList<String> request = new ArrayList<>();

            // Get a message from the client, loops until a message is received
            while(running) {
                System.out.println("["+Thread.currentThread()+"] "+"checking for response");
                try {
                    // check for incoming responses
                    while(input.ready()) {
                        request.add(input.readLine());
                    }

                    if(request.size() != 0) {
                        // process request here
                        System.out.println("["+Thread.currentThread()+"] "+request.get(0));
                        processRequest(request);
                        request.clear();
                        System.out.println("["+Thread.currentThread()+"] "+"processed request");
                    }
                    running = false;

                } catch (IOException e) {
                    System.out.println("Failed to receive msg from the client");
                    e.printStackTrace();
                }
            }

            // close the socket
            try {
                input.close();
                output.close();
                client.close();
            } catch (Exception e) {
                System.out.println("Failed to close socket");
            }
            System.out.println("["+Thread.currentThread()+"] "+"Ending thread");
        }

        /**
         * Processes a request and sends a response to the client
         *
         * @param request The request, split by the line separator
         */
        private void processRequest(ArrayList<String> request) {
            if(request.get(0).startsWith("GET")) {
                String[] firstLine = request.get(0).split(" ");
                // resource path is stored in firstLine[1]
                String path = firstLine[1];

                if(path.equals("/favicon.ico")) {
                    output.println("HTTP/1.1 404 Not Found");
                    output.flush();
                    return;
                }

                StringBuilder content = new StringBuilder();
                content.append("<html>");
                content.append("<head>");
                content.append("</head>");
                content.append("<body>");
                if(path.equals("/")) {// homepage

                    content.append(new Hyperlink("/login/", "Log in").toHTMLString());
                    content.append(new Hyperlink("/signup/", "Sign up").toHTMLString());

                } else if(path.equals("/login/")) {// login page
                    content.append("<p>Username</p>");
                    content.append("<p>Password</p>");
                } else if(path.equals("/signup/")) {// signup page

                }
                content.append("</body>");
                content.append("</html>");


                output.println("HTTP/1.1 200 OK");
                output.println("Content-Type: text/html");
                output.println("Content-Length: " + content.length());
                output.println();
                output.println(content.toString());
                output.flush();
            }
        }

    }
}
