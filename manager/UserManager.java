package manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import struct.User;

import utility.Pair;
import utility.SQLStatementBuilder;

/**
 * A class that manages {@code User} objects
 *
 * @author Ricky Qin
 */
public class UserManager extends DatabaseManager {

    private HashMap<Integer, User> cache;

    @Override
    public Object getById(int id) {
        User cacheResult = cache.get(id);
        if(cacheResult != null) {
            return cacheResult;
        }
        ArrayList<? extends Object> dbResult = executeReadOperation(new SQLStatementBuilder()
                .select().from("USERS").where("ID="+id).toString());
        if(dbResult.size() == 1) {
            User user = (User)(dbResult.get(1));
            cache.put(user.getID(), user);
            return user;
        } else {
            return null;
        }
    }

    /**
     * Constructs a UserManager class for the specified database
     *
     * @param dbName The name of database to manage (must include {@code .db} file extension)
     */
    public UserManager(String dbName) {
        super(dbName);
        //TODO Auto-generated constructor stub
    }

    /**
     * Executes an operation that reads from the database
     * @param statement An SQL statement.
     * @return An ArrayList containing all the data.
     * */
    @Override
    public ArrayList<? extends Object> executeReadOperation(String statement) {
        Pair<ResultSet, Statement> result = getReadOperationResultSet(statement);
        try {
            ResultSet rs = result.first();
            ArrayList<User> list = new ArrayList<User>();
            while(rs.next()) {
                int id = rs.getInt("ID");
                String username = rs.getString("USERNAME");

                User user = new User(id, username, this);
                cache.put(id, user);
                list.add(user);
            }
            result.second().close();
            return list;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Puts a user's information in the database.
     * @param username The user's username
     * @param password The user's password
     * @return Whether the entering of information is successful or not.
     * */
    public boolean registerUser (String username, String password) {
        String valueList = "(" + username + ", "+ password + ")";
        try {
            return executeWriteOperation(new SQLStatementBuilder().insertInto("USERS", "(USERNAME, PASSWORD)").values(valueList).toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser (User user) {
        try {
            return executeWriteOperation(new SQLStatementBuilder().deleteFrom("USERS").where("USERNAME="+user.getUsername()).toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User login (String username, String password) {
        ArrayList<? extends Object> dbResult = executeReadOperation(new SQLStatementBuilder()
                .select().from("USERS").where("USERNAME="+username+" AND PASSWORD="+password).toString()); // SELECT FROM USERS WHERE USERNAME = 123 AND PASSWORD = 123
        if(dbResult.size() == 1) {
            User user = (User)(dbeRsult.get(1));
            cache.put(user.getID(), user);
            return user;
        } else {
            return null;
        }
    }

    public boolean changeUsername (String username, String newUsername, String password) {
        
        return false;
    }

    public boolean changePassword (String username, String password, String newPassword) {
        return false;
    }

    /**
     * Authenticates a user by checking the provided credentials against the database.
     *
     * @param username The username
     * @param password The password
     * @return The User object or {@code null} if the credentials do not match
     */
    public User authenticateUser(String username, String password) {
        ArrayList<? extends Object> list = executeReadOperation(new SQLStatementBuilder().select()
                .from("USERS").where("USERNAME="+username+" AND PASSWORD="+password).toString());
        if(list != null) {
            return (User)(list.get(0));
        } else {
            return null;
        }
    }

}
