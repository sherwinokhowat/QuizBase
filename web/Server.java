package web;

import manager.QuizManager;
import manager.UserManager;

public class Server {

    private UserManager userManager;

    private QuizManager quizManager;

    private ServerSocket serverSocket;// server socket for connection

    private static boolean running = true;  // controls if the server is accepting clients
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
        System.out.println("Waiting for a client connection..");

        Socket client = null;//hold the client connection

        try {
            serverSocket = new ServerSocket(5000);  //assigns an port to the server
            // serverSocket.setSoTimeout(15000);  //5 second timeout
            while(accepting) {  //this loops to accept multiple clients
                client = serverSocket.accept();  //wait for connection
                System.out.println("Client connected");
                //Note: you might want to keep references to all clients if you plan to broadcast messages
                //Also: Queues are good tools to buffer incoming/outgoing messages
                Thread t = new Thread(new ConnectionHandler(client)); //create a thread for the new client and pass in the socket
                t.start(); //start the new thread
            }
        }catch(Exception e) {
            System.out.println("Error accepting connection");
            try {
                client.close();
            }catch (Exception e1) {
                System.out.println("Failed to close socket");
            }
            System.exit(-1);
        }

    }
}
