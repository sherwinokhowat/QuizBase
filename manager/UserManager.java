package manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import struct.User;

import utility.Pair;
import utility.SQLStatementBuilder;

/**
 * A class that manages {@code User} objects
 *
 * @author Ricky Qin, Sherwin Okhowat, and Avery Chan
 */
public class UserManager extends DatabaseManager {

    @Override
    public Object getById(int id) {
        ArrayList<? extends Object> dbResult = executeReadOperation(new SQLStatementBuilder()
                .select().from("USERS").where("ID="+id).toString());
        if (dbResult.size() == 1) {
            User user = (User) (dbResult.get(0));
            return user;
        } else {
            return null;
        }
    }

    /**
     * Constructs a UserManager class for the specified database
     *
     * @param dbName The name of database to manage (must include {@code .db} file
     *               extension)
     */
    public UserManager(String dbName) {
        super(dbName);
    }

    @Override
    public void initialize() {
        StringBuilder statement = new StringBuilder();
        statement.append("CREATE TABLE IF NOT EXISTS USERS (");
        statement.append("ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        statement.append("USERNAME TEXT NOT NULL UNIQUE CHECK(LENGTH(USERNAME) > 2),");
        statement.append("PASSWORD TEXT NOT NULL CHECK(LENGTH(PASSWORD) > 2)");
        statement.append(");");
        executeWriteOperation(statement.toString());
    }

    /**
     * Executes an operation that reads from the database
     *
     * @param statement An SQL statement.
     * @return An ArrayList containing all the data.
     */
    @Override
    public ArrayList<? extends Object> executeReadOperation(String statement) {
        Pair<ResultSet, Statement> result = getReadOperationResultSet(statement);
        try {
            ResultSet rs = result.first();
            ArrayList<User> list = new ArrayList<User>();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String username = rs.getString("USERNAME");

                User user = new User(id, username, this);
                list.add(user);
            }
            result.second().close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Puts a user's information in the database.
     *
     * @param username The user's username
     * @param password The user's password
     * @return The User object or {@code null} if registration was not successful.
     */
    public User registerUser(String username, String password) {
        boolean successful = executeWriteOperation(
                new SQLStatementBuilder().insertInto("USERS", "USERNAME", "PASSWORD")
                .values("'"+username+"'", "'"+password+"'").toString());
        if(successful) {
            ArrayList<? extends Object> list = executeReadOperation(new SQLStatementBuilder().select()
                    .from("USERS").where("USERNAME='"+username+"'").toString());
            return (User)list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Deletes the user from the database
     *
     * @param user The user to delete
     * @return Whether or not the deletion is successful
     */
    public boolean deleteUser(User user) {
        return executeWriteOperation(
                new SQLStatementBuilder().deleteFrom("USERS")
                .where("USERNAME='" + user.getUsername() + "'").toString());
    }

    /**
     * Changes the user's respective username in the database and placeholder user
     * object after authenticating the user
     *
     * @param username    the user's current username
     * @param newUsername the user's requested username change
     * @param password    the user's current password
     * @return
     */
    public boolean changeUsername(String username, String newUsername, String password) {
        User user = authenticateUser(username, password);
        if (user != null) {
            boolean isSuccessful = executeWriteOperation(new SQLStatementBuilder().update("USERS")
                    .set(new Pair<String, String>("USERNAME", newUsername))
                    .where("USERNAME='" + username + "'").toString());
            if (isSuccessful) {
                user.setUsername(newUsername);
            }
            return isSuccessful;
        }
        return false;
    }

    /**
     * Changes the user's respective password in the database after authenticating
     * the user
     *
     * @param username    the user's current username
     * @param password    the user's current password
     * @param newPassword the user's requested password change
     * @return whether or not the password change was successful
     */
    public boolean changePassword(String username, String password, String newPassword) {
        if (this.authenticateUser(username, password) != null) {
            // change password in the database
            boolean isSuccessful = executeWriteOperation(new SQLStatementBuilder().update("USERS")
                    .set(new Pair<String, String>("PASSWORD", newPassword))
                    .where("USERNAME='" + username + "'").toString());
            return isSuccessful;
        }
        return false;
    }

    /**
     * Authenticates a user by checking the provided credentials against the
     * database.
     *
     * @param username The username
     * @param password The password
     * @return The User object or {@code null} if the credentials do not match
     */
    public User authenticateUser(String username, String password) {
        ArrayList<? extends Object> list = executeReadOperation(new SQLStatementBuilder().select()
                .from("USERS").where("USERNAME='" + username + "' AND PASSWORD='" + password + "'")
                .toString());
        if (list.size() == 1) {
            return (User) (list.get(0));
        } else {
            return null;
        }
    }

}
