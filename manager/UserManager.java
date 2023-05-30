package manager;

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
    public boolean insertToDatabase(String table, Object obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertToDatabase'");
    }

    @Override
    public Object selectFromDatabase(String table, String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectFromDatabase'");
    }

    @Override
    public boolean writeToDatabase(String table, String id, Pair<String, Object>... args) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'writeToDatabase'");
    }

    @Override
    public boolean deleteFromDatabase(String table, String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFromDatabase'");
    }

}
