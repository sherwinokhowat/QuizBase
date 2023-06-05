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
 * @author Ricky Qin
 */
public class UserManager extends DatabaseManager {

    /**
     * Constructs a UserManager class for the specified database
     *
     * @param dbName The name of database to manage (must include {@code .db} file extension)
     */
    public UserManager(String dbName) {
        super(dbName);
        //TODO Auto-generated constructor stub
    }

    @Override
    public ArrayList<? extends Object> executeReadOperation(String statement) {
        Pair<ResultSet, Statement> result = getReadOperationResultSet(statement);
        try {
            ResultSet rs = result.first();
            ArrayList<User> list = new ArrayList<User>();
            while(rs.next()) {
                int id = rs.getInt("ID");
                String username = rs.getString("USERNAME");
                list.add(new User(id, username, this));
            }
            result.second().close();
            return list;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
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
