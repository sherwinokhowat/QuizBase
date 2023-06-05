package manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import struct.User;

import utility.Pair;

/**
 * A class that manages {@code User} objects
 *
 * @author Ricky Qin
 */
public class UserManager extends DatabaseManager {

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
                int id = rs.getInt("id");
                String username = rs.getString("username");
                list.add(new User(username));
            }
            result.second().close();
            return list;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
