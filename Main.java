import web.Server;

/**
 * Main class used to start the program.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Directory: " + System.getProperty("user.dir"));
        new Server().start( "storage.db");
    }
}
