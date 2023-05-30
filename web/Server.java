package web;

import manager.QuizManager;
import manager.UserManager;

public class Server {

    private UserManager userManager;

    private QuizManager quizManager;

    /**
     * Starts this server.
     *
     * @param port The port number
     * @param dbName The database name (must include {@code .db} file extension)
     */
    public void start(int port, String dbName) {
        userManager = new UserManager(dbName);
        quizManager = new QuizManager(dbName);
    }
}
