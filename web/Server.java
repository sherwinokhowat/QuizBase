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

public class Server {

    private UserManager userManager;

    private QuizManager quizManager;

    private ServerSocket serverSock;// server socket for connection

    private ArrayList<Thread> threadList;

    // private static boolean running = true;  // controls if the server is accepting clients
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
            serverSock = new ServerSocket(5000);  //assigns an port to the server
            // serverSock.setSoTimeout(15000);  //5 second timeout
            while(accepting) {  //this loops to accept multiple clients
                client = serverSock.accept();  //wait for connection
                System.out.println("Client connected");
                //Note: you might want to keep references to all clients if you plan to broadcast messages
                //Also: Queues are good tools to buffer incoming/outgoing messages
                Thread t = new Thread(new ConnectionHandler(client)); //create a thread for the new client and pass in the socket
                t.start(); //start the new thread

                // accepting = false;
            }
        } catch(Exception e) {
            System.out.println("Error accepting connection");
            //close all and quit
            try {
                client.close();
            }catch (Exception e1) {
                System.out.println("Failed to close socket");
            }
            System.exit(-1);
        }
    }

    //***** Inner class - thread for client connection
    class ConnectionHandler implements Runnable {

        private PrintWriter output; //assign printwriter to network stream
        private BufferedReader input; //Stream for network input
        private Socket client;  //keeps track of the client socket
        private boolean running;

        //HTML stuff ??


        /**
         * Constructs a ConnectionHandler
         * @param s the socket belonging to this client connection
         */
        ConnectionHandler(Socket sock) {
            this.client = sock;  //constructor assigns client to this
            try {  //assign all connections to client
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
            // Get a message from the client
            ArrayList<String> request = new ArrayList<>();

            // Get a message from the client, loops until a message is received
            while(running) {
                try {
                    // check for incoming responses
                    while(input.ready()) {
                        request.add(input.readLine());
                    }

                    if(request.size() != 0) {
                        // process request here
                        System.out.println(request);
                        processRequest(request);
                        request.clear();
                    }

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
                System.out.println(path);

                if(path.equals("/favicon.ico")) {
                    output.println("404 Not Found");
                    output.flush();
                    return;
                }
                StringBuilder content = new StringBuilder();
                content.append("<html>");
                content.append("<head>");
                content.append("</head>");
                content.append("<body>");
                content.append(new Hyperlink("/login/", "Log in").toHTMLString());
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
