package manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import struct.Quiz;
import struct.User;
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
     */
    public QuizManager(String dbName, UserManager userManager) {
        super(dbName);
        this.userManager = userManager;
    }

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
     * Adds a quiz to the database
     *
     * @param creator the id of the user that created it.
     * @param name The name
     * @param description The description
     * @return The created Quiz or {@code null} if an error occurred
     */
    public Quiz addQuiz(int creator, String name, String description) {
        boolean successful = executeWriteOperation(new SQLStatementBuilder().insertInto("QUIZZES", "NAME", "DESCRIPTION", "CREATOR_ID").values("'" + name + "'", "'" + description + "'", String.valueOf(creator)).toString());
        if(successful) {
            ArrayList<? extends Object> list = executeReadOperation(new SQLStatementBuilder().select()
                    .from("QUIZZES").where("NAME='"+name+"'").toString());
            return (Quiz)list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Deletes a quiz based on the User who made it and
     * @param requestor
     * @param quiz
     * @return whether deletion was successful or not.
     */
    public boolean deleteQuiz (User requestor, Quiz quiz) {
        return executeWriteOperation(new SQLStatementBuilder().deleteFrom("QUIZZES").where("ID='"+quiz.getID() + "' AND CREATOR_ID='" + requestor.getID() + "'").toString());
    }

    /**
     * Returns the quiz with a certain ID and Name
     * @param id the quiz's ID
     * @param name the name of the quiz (case sensitive)
     * @return
     */
    public Quiz getQuiz (int id, String name) {
        ArrayList<? extends Object> dbResult = executeReadOperation(new SQLStatementBuilder()
        .select().from("QUIZZES")
        .where("ID='" + id + "' AND NAME='" + name + "'").toString());
        if (dbResult.size() == 1) {
            return (Quiz) dbResult.get(1);
        } else {
            return null;
        }
    }

    /**
     * Adds a Flashcard to the database
     *
     * @param quizID The id of the quiz associated with the Flashcard
     * @param question The question on the Flashcard
     * @param answer The answer
     * @return If the operation was successful
     */
    public boolean addFlashCard(int quizID, String question, String answer) {
        return executeWriteOperation(new SQLStatementBuilder()
                .insertInto("QUIZ_ITEMS", "QUIZ_ID", "TYPE", "QUESTION", "OPTION_1")
                .values(Integer.toString(quizID), Integer.toString(FLASHCARD), "'"+question+"'", "'"+answer+"'").toString());
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
     * @return If the operation was successful
     */
    public boolean addMultipleChoice(int quizID, String question, String option1, String option2, String option3, String option4, int answer) {
        return executeWriteOperation(new SQLStatementBuilder()
                .insertInto("QUIZ_ITEMS", "QUIZ_ID", "TYPE", "QUESTION", "OPTION_1", "OPTION_2", "OPTION_3", "OPTION_4", "CORRECT_ANSWER")
                .values(Integer.toString(quizID), Integer.toString(MULTIPLE_CHOICE), "'"+question+"'", "'"+option1+"'", "'"+option2+"'", "'"+option3+"'", "'"+option4+"'", Integer.toString(answer)).toString());
    }

    @Override
    public Object getBy(String columnName, String columnValue) {
        ArrayList<? extends Object> dbResult = executeReadOperation(new SQLStatementBuilder()
                .select().from("QUIZZES").where(columnName+"="+columnValue).toString());
        if(dbResult.size() == 1) {
            Quiz quiz = (Quiz)(dbResult.get(0));
            return quiz;
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
                int creatorId = rs.getInt("CREATOR_ID");
                list.add(new Quiz(id, name, description, creatorId, this, userManager));
            }
            result.second().close();
            return list;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns all the quizzes created by a user.
     * @param user The user
     * @return an ArrayList containing all the quizzes returned by a user.
     */
    public ArrayList<? extends Object> getUserCreatedQuizzes(User user) {
        return executeReadOperation(new SQLStatementBuilder().select().from("QUIZZES").where("CREATOR_ID="+user.getID()).toString());
    }

    /**
     * Returns all the quizzes created.
     * @return an ArrayList containing all the quizzes stored in the database.
     */
    public ArrayList<? extends Object> getAllCreatedQuizzes() {
        return executeReadOperation(new SQLStatementBuilder().select().from("QUIZZES").toString());
    }


}
