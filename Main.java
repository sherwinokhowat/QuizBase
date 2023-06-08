import utility.Pair;
import utility.SQLStatementBuilder;
import web.*;

public class Main {
    public static void main(String[] args) {
        // TODO start program here
        System.out.println(new SQLStatementBuilder().insertInto("QUIZZES").values("1", "Biology", "10", "2.5"));
        System.out.println(new SQLStatementBuilder().select("ID", "NAME", "LENGTH", "DIFFICULTY").from("QUIZZES").where("LENGTH <= 10").limit(10));
        System.out.println(new SQLStatementBuilder().select("ID", "NAME", "LENGTH", "DIFFICULTY").from("QUIZZES").orderBy(new Pair<>("DIFFICULTY", SQLStatementBuilder.DESCENDING), new Pair<>("NAME", SQLStatementBuilder.ASCENDING)));
        System.out.println(new SQLStatementBuilder().select().from("USERS"));
        System.out.println(new SQLStatementBuilder().update("QUIZZES").set(new Pair<>("DIFFULTY", "3.5")).where("ID = 1"));
        System.out.println(new SQLStatementBuilder().deleteFrom("USERS").where("ID = 42"));
        new Server().start(3000, "storage.db");
    }
}
