package manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import utility.Pair;

/**
 * An abstract class that has access to a database.
 *
 * @author Ricky Qin
 */
public abstract class DatabaseManager {

    protected String dbName = null;

    protected Connection connection = null;

    /**
     * Constructs a DatabaseManager object
     *
     * @param dbName The name of database to manage (must include {@code .db} file extension)
     */
    public DatabaseManager(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Attempts to connect to the database with the name {@code name}.
     *
     * @return {@code true} if connection is successful and {@code false} if an {@code SQLException} occurs
     */
    public boolean connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+dbName);
            System.out.println("Connected to Database");
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserts the specified object into the database
     *
     * @param table The table to add the object to
     * @param obj The obj to add
     * @return {@code true} if the operation was successful
     */
    public abstract boolean insertToDatabase(String table, Object obj);

    /**
     * Selects the specified object into the database
     *
     * @param table The table to select the object from
     * @param id The id of the object to retrieve
     * @return The selected object or {@code null} if it was not found
     */
    public abstract Object selectFromDatabase(String table, String id);

    /**
     * Writes (modifies) the specified object in the database
     *
     * @param table The table where the obj is located
     * @param id The id of the object to modify
     * @param args The fields within the object to modify. Each first value in the pair is the name of the field and the second value is the new value.
     * @return {@code true} if the operation was successful
     */
    public abstract boolean writeToDatabase(String table, String id, Pair<String, Object>... args);

    /**
     * Writes (modifies) the specified object in the database
     *
     * @param table The table where the obj is located
     * @param id The id of the object to remove
     * @return {@code true} if the operation was successful
     */
    public abstract boolean deleteFromDatabase(String table, String id);
}
