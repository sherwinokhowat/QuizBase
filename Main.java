import web.Server;

public class Main {
    public static void main(String[] args) {
        System.out.println("Directory: " + System.getProperty("user.dir"));
        new Server().start(3000, "storage.db");
    }
}
