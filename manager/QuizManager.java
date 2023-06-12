package manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import struct.*;
import utility.Pair;
import utility.SQLStatementBuilder;

/**
 * A class that manages {@code Quiz} and {@code QuizItem} objects
 *
 * @author Ricky Qin and Sherwin Okhowat
 */
public class QuizManager extends DatabaseManager {

    private static int FLASHCARD = 1;
    private static int MULTIPLE_CHOICE = 2;

    private UserManager userManager;

    /**
     * Constructs a QuizManager class for the specified database
     *
     * @param dbName The name of database to manage (must include {@code .db} file extension)
     * @param userManager An instance of the User's database manager
     */
    public QuizManager(String dbName, UserManager userManager) {
        super(dbName);
        this.userManager = userManager;
    }

    /**
     * Initializes the SQL tables
     */
    @Override
    public void initialize() {
        StringBuilder statement = new StringBuilder();
        statement.append("CREATE TABLE IF NOT EXISTS QUIZZES (");
        statement.append("ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        statement.append("NAME TEXT NOT NULL UNIQUE CHECK(LENGTH(NAME) > 0),");
        statement.append("DESCRIPTION TEXT,");
        statement.append("CREATOR_ID INTEGER NOT NULL,");
        statement.append("FOREIGN KEY (CREATOR_ID) REFERENCES USERS (ID)");
        statement.append(");");
        executeWriteOperation(statement.toString());

        StringBuilder statement2 = new StringBuilder();
        statement2.append("CREATE TABLE IF NOT EXISTS QUIZ_ITEMS (");
        statement2.append("ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        statement2.append("QUIZ_ID INTEGER NOT NULL,");
        statement2.append("TYPE INTEGER NOT NULL CHECK(TYPE >= 1 AND TYPE <= 2),");
        statement2.append("QUESTION TEXT NOT NULL CHECK(LENGTH(QUESTION) > 0),");
        statement2.append("OPTION_1 TEXT NOT NULL CHECK(LENGTH(OPTION_1) > 0),");
        statement2.append("OPTION_2 TEXT CHECK(OPTION_2 IS NULL OR (LENGTH(OPTION_2) > 0 AND OPTION_1 IS NOT NULL)),");
        statement2.append("OPTION_3 TEXT CHECK(OPTION_3 IS NULL OR (LENGTH(OPTION_3) > 0 AND OPTION_2 IS NOT NULL)),");
        statement2.append("OPTION_4 TEXT CHECK(OPTION_4 IS NULL OR (LENGTH(OPTION_4) > 0  AND OPTION_3 IS NOT NULL)),");
        statement2.append("CORRECT_ANSWER INTEGER CHECK((CORRECT_ANSWER IS NULL AND OPTION_2 IS NULL) OR (CORRECT_ANSWER >= 1 AND CORRECT_ANSWER <= 4)),");
        statement2.append("FOREIGN KEY (QUIZ_ID) REFERENCES QUIZZES (ID)");
        statement2.append(");");
        executeWriteOperation(statement2.toString());
    }

    /**
     * Adds a quiz to the database.
     *
     * @param creator the id of the user that created it.
     * @param name The name of the quiz.
     * @param description The description of the quiz.
     * @return The created Quiz or {@code null} if an error occurred
     */
    public Quiz addQuiz(int creator, String name, String description) {
        boolean successful = executeWriteOperation(new SQLStatementBuilder()
                .insertInto("QUIZZES", "NAME", "DESCRIPTION", "CREATOR_ID")
                .values(name, description, creator).toString());
        if(successful) {
            ArrayList<Quiz> list = executeReadOperation(new SQLStatementBuilder().select()
                    .from("QUIZZES").where("NAME="+SQLStatementBuilder.toStringLiteral(name)).toString());
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Deletes a quiz based on the User who made it.
     *
     * @param requestor the User who created the quiz.
     * @param quiz An instance of the quiz.
     * @return whether deletion was successful or not.
     */
    public boolean deleteQuiz (User requestor, Quiz quiz) {
        return executeWriteOperation(new SQLStatementBuilder().deleteFrom("QUIZZES")
                .where("ID="+quiz.getID() + " AND CREATOR_ID=" + requestor.getID()).toString());
    }

    /**
     * Adds a Flashcard to the database.
     *
     * @param quizID The id of the quiz associated with the Flashcard.
     * @param question The question on the Flashcard.
     * @param answer The answer on the flashcard.
     * @return If the operation was successful.
     */
    public boolean addFlashCard(int quizID, String question, String answer) {
        return executeWriteOperation(new SQLStatementBuilder()
                .insertInto("QUIZ_ITEMS", "QUIZ_ID", "TYPE", "QUESTION", "OPTION_1")
                .values(quizID, FLASHCARD, question, answer).toString());
    }

    /**
     * Adds a Multiple Choice to the database
     *
     * @param quizID The id of the quiz associated with the Multiple Choice
     * @param question The question on the Multiple Choice
     * @param option1 The first option
     * @param option2 The second option
     * @param option3 The third option
     * @param option4 The fourth option
     * @param answer The number of the correct answer
     * @return Whether the operation was successful
     */
    public boolean addMultipleChoice(int quizID, String question, String option1, String option2, String option3, String option4, int answer) {
        return executeWriteOperation(new SQLStatementBuilder()
                .insertInto("QUIZ_ITEMS", "QUIZ_ID", "TYPE", "QUESTION", "OPTION_1", "OPTION_2", "OPTION_3", "OPTION_4", "CORRECT_ANSWER")
                .values(quizID, MULTIPLE_CHOICE, question, option1, option2, option3, option4, answer).toString());
    }


    /**
     * Fetches the database for the Quiz with the specified value in the specified column.
     * Recommended that this is used on columns with unique values.
     *
     * @param columnName The column name
     * @param columnValue The column value. Non-integer objects will be converted to a string
     * by calling the object's {@code toString()} method.
     * @return The found Quiz or {@null} if it does not exist.
     */
    @Override
    public Quiz getBy(String columnName, Object columnValue) {
        if(columnValue instanceof Integer) {
            columnValue = ((Integer)columnValue).toString();
        } else {
            columnValue = SQLStatementBuilder.toStringLiteral(columnValue.toString());
        }
        ArrayList<Quiz> dbResult = executeReadOperation(new SQLStatementBuilder()
                .select().from("QUIZZES")
                .where(columnName+"="+columnValue).toString());
        if(dbResult.size() > 0) {
            return dbResult.get(0);
        } else {
            return null;
        }
    }

    /**
     * Fetches the database for the QuizItem with the specified value in the specified column.
     * Recommended that this is used on columns with unique values.
     *
     * @param columnName The column name
     * @param columnValue The column value. Non-integer objects will be converted to a string
     * by calling the object's {@code toString()} method.
     * @return The found QuizItem or {@null} if it does not exist. If multiple objects have
     * the same value, any one of them is returned.
     */
    public QuizItem getQuizItemBy(String columnName, Object columnValue) {
        if(columnValue instanceof Integer) {
            columnValue = ((Integer)columnValue).toString();
        } else {
            columnValue = SQLStatementBuilder.toStringLiteral(columnValue.toString());
        }
        ArrayList<QuizItem> dbResult = executeReadQuizItemOperation(new SQLStatementBuilder()
                .select().from("QUIZ_ITEMS")
                .where(columnName+"="+columnValue).toString());
        if(dbResult.size() > 0) {
            return dbResult.get(0);
        } else {
            return null;
        }
    }

    /**
     * Returns a list of Quiz objects based on the SQL {@code SELECT} statement. Implementing classes must ensure that
     *  <ul>
     *  <li>{@code getReadOperationResultSet(String statement)} is called</li>
     *  <li>The method correctly parses the ResultSet into the appropriate class object</li>
     *  <li>At the end of the method, {@code Transaction.close()} is called on the
     *  Transaction object</li>
     *  </ul>
     *
     * @param statement The SQL statement
     * @return An array of parsed Quiz objects or {@code null} if an exception occurred.
     */
    @Override
    public ArrayList<Quiz> executeReadOperation(String statement) {
        Pair<ResultSet, Statement> resultQuiz = getReadOperationResultSet(statement);
        try {
            ResultSet rsQuiz = resultQuiz.first();
            ArrayList<Quiz> list = new ArrayList<Quiz>();
            while(rsQuiz.next()) {
                int id = rsQuiz.getInt("ID");
                String name = rsQuiz.getString("NAME");
                String description = rsQuiz.getString("DESCRIPTION");
                int creatorID = rsQuiz.getInt("CREATOR_ID");
                list.add(new Quiz(id, name, description, creatorID, this, userManager));
            }
            resultQuiz.second().close();
            return list;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Runs a SQL {@code SELECT} statement to select QuizItems. Implementing classes must ensure that
     * <ul>
     *  <li>{@code getReadOperationResultSet(String statement)} is called</li>
     *  <li>The method correctly parses the ResultSet into the appropriate class object</li>
     *  <li>At the end of the method, {@code Transaction.close()} is called on the
     * Transaction object</li>
     * </ul>
     *
     * @param statement The SQL statement
     * @return An array of QuizItems or {@code null} if an exception occurred.
     */
    public ArrayList<QuizItem> executeReadQuizItemOperation(String statement) {
        Pair<ResultSet, Statement> resultQuiz = getReadOperationResultSet(statement);
        try {
            ResultSet rs = resultQuiz.first();
            ArrayList<QuizItem> items = new ArrayList<>();
            while(rs.next()) {
                int id = rs.getInt("ID");
                int quizID = rs.getInt("QUIZ_ID");
                int type = rs.getInt("TYPE");
                String question = rs.getString("QUESTION");
                if(type == FLASHCARD) {
                    String answer = rs.getString("OPTION_1");
                    items.add(new Flashcard(id, quizID, question, answer));
                } else if (type == MULTIPLE_CHOICE) {
                    String option1 = rs.getString("OPTION_1");
                    String option2 = rs.getString("OPTION_2");
                    String option3 = rs.getString("OPTION_3");
                    String option4 = rs.getString("OPTION_4");
                    int answer = rs.getInt("CORRECT_ANSWER");
                    items.add(new MultipleChoice(id, quizID, question, option1, option2, option3, option4, answer));
                }
            }
            resultQuiz.second().close();
            return items;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the list of IDs of the quiz items in a quiz
     *
     * @param quizID The id of the quiz
     * @return An arraylist of IDs
     */
    public ArrayList<Integer> getQuizItemIDS(int quizID) {
        Pair<ResultSet, Statement> resultQuiz = getReadOperationResultSet(new SQLStatementBuilder()
                .select("ID").from("QUIZ_ITEMS").where("QUIZ_ID="+quizID).toString());
        try {
            ResultSet rs = resultQuiz.first();
            ArrayList<Integer> items = new ArrayList<>();
            while(rs.next()) {
                items.add(rs.getInt("ID"));

            }
            resultQuiz.second().close();
            return items;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns all the quizzes created by a user.
     *
     * @param user The user.
     * @return an ArrayList containing all the quizzes returned by a user.
     */
    public ArrayList<Quiz> getUserCreatedQuizzes(User user) {
        return executeReadOperation(new SQLStatementBuilder().select().from("QUIZZES")
                .where("CREATOR_ID="+user.getID()).toString());
    }

    /**
     * Returns all the quizzes created.
     *
     * @return an ArrayList containing all the quizzes stored in the database.
     */
    public ArrayList<Quiz> getAllCreatedQuizzes() {
        return executeReadOperation(new SQLStatementBuilder().select().from("QUIZZES").toString());
    }


}
