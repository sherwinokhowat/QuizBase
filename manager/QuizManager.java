package manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import struct.Quiz;
import utility.Pair;
import utility.SQLStatementBuilder;

/**
 * A class that manages {@code Quiz} objects
 *
 * @author Ricky Qin
 */
public class QuizManager extends DatabaseManager {

    private HashMap<Integer, Quiz> cache = new HashMap<Integer, Quiz>();

    /**
     * Constructs a QuizManager class for the specified database
     *
     * @param dbName The name of database to manage (must include {@code .db} file extension)
     */
    public QuizManager(String dbName) {
        super(dbName);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void initialize() {
        StringBuilder statement = new StringBuilder();
        statement.append("CREATE TABLE IF NOT EXISTS QUIZZES (");
        statement.append("ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        statement.append("NAME TEXT NOT NULL CHECK(LENGTH(NAME) > 0),");
        statement.append("DESCRIPTION TEXT,");
        statement.append("CREATOR_ID INTEGER NOT NULL,");
        statement.append("FOREIGN KEY (CREATOR_ID) REFERENCES USERS (ID)");
        statement.append(");");
        executeWriteOperation(statement.toString());
    }

    @Override
    public Object getById(int id) {
        Quiz cacheResult = cache.get(id);
        if(cacheResult != null) {
            return cacheResult;
        }
        ArrayList<? extends Object> dbResult = executeReadOperation(new SQLStatementBuilder()
                .select().from("QUIZZES").where("ID="+id).toString());
        if(dbResult.size() == 1) {
            Quiz user = (Quiz)(dbResult.get(1));
            cache.put(user.getID(), user);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<? extends Object> executeReadOperation(String statement) {
        Pair<ResultSet, Statement> result = getReadOperationResultSet(statement);
        try {
            ResultSet rs = result.first();
            ArrayList<Quiz> list = new ArrayList<Quiz>();
            while(rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String description = rs.getString("DESCRIPTION");
                int creatorId = rs.getInt("CREATOR");
                list.add(new Quiz(id, name, description, creatorId, this));
            }
            result.second().close();
            return list;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
