package manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
     * Retrieves the object with the specified id. If it exists in the cache, that reference is returned.
     * If it does not exist in the cache, it fetches the database for the object and returns the result.
     *
     * @param id The object id
     * @return The found object or {@null} if it does not exist in the database
     */
    public abstract Object getById(int id);

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
     * Initializes the database with the specified tables (if the tables do not exist)
     */
    public abstract void initialize();

    /**
     * Runs a SQL statement that is one of {@code CREATE TABLE}, {@code DROP TABLE},
     * {@code INSERT INTO}, {@code UPDATE} or {@code DELETE FROM}
     *
     * @param statement The SQL statement
     * @return {@code true} if the operation was successful
     */
    public boolean executeWriteOperation(String statement) {
        Statement transaction = null;
        try {
            transaction = connection.createStatement();
            transaction.executeUpdate(statement);
            transaction.close();
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * An internal method that Runs a SQL {@code SELECT} statement and retrieves the returned
     * ResultSet.
     *
     * @param statement The SQL statement
     * @return A Pair object storing two values:
     * <ul>
     *  <li>A ResultSet representing the results of the
     * execution.</li>
     *  <li>The Transaction object.</li>
     * </ul>
     * {@code null} is returned if an exception occurred.
     */
    protected Pair<ResultSet, Statement> getReadOperationResultSet(String statement) {
        Statement transaction = null;
        try {
            transaction = connection.createStatement();
            return new Pair<>(transaction.executeQuery(statement), transaction);
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Runs a SQL {@code SELECT} statement. Implementing classes must ensure that
     * <ul>
     *  <li>{@code .getReadOperationResultSet(String statement)} is called</li>
     *  <li>This method correct parses the ResultSet into the appropriate class object</li>
     *  <li>At the end of this method, {@code Transaction.close()} is called on the
     * Transaction object</li>
     * <li>A caching mechanism is used to ensure that there is one instance of each object.
     * Objects retrieved from the database should be stored in the cache</li>
     * </ul>
     *
     * @param statement The SQL statement
     * @return An array of objects or {@code null} if an exception occurred.
     */
    public abstract ArrayList<? extends Object> executeReadOperation(String statement);
}
