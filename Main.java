import web.Server;

public class Main {
    public static void main(String[] args) {
        new Server().start(3000, "storage.db");
    }
}
