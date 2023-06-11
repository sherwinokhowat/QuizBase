package manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.PriorityQueue;

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
        boolean successful = executeWriteOperation(new SQLStatementBuilder()
                .insertInto("QUIZZES", "NAME", "DESCRIPTION", "CREATOR_ID")
                .values(name, description, creator).toString());
        if(successful) {
            ArrayList<? extends Object> list = executeReadOperation(new SQLStatementBuilder().select()
                    .from("QUIZZES").where("NAME="+SQLStatementBuilder.toStringLiteral(name)).toString());
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
        return executeWriteOperation(new SQLStatementBuilder().deleteFrom("QUIZZES")
                .where("ID="+quiz.getID() + " AND CREATOR_ID=" + requestor.getID()).toString());
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
                .where("ID=" + id + " AND NAME=" + SQLStatementBuilder.toStringLiteral(name)).toString());
        if (dbResult.size() == 1) {
            return (Quiz) dbResult.get(0);
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
                .values(quizID, FLASHCARD, question, answer).toString());
    }

    public ArrayList<? extends Object> getQuizItems(int quizID) {
        ArrayList<? extends Object> dbResult = executeReadOperation(new SQLStatementBuilder()
                .select().from("QUIZ_ITEMS").where("QUIZ_ID="+quizID).toString());

        return (dbResult == null) ? null : dbResult;
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
                .values(quizID, MULTIPLE_CHOICE, question, option1, option2, option3, option4, answer).toString());
    }

    @Override
    public Object getBy(String columnName, Object columnValue) {
        if(columnValue instanceof Integer) {
            columnValue = ((Integer)columnValue).toString();
        } else {
            columnValue = SQLStatementBuilder.toStringLiteral(columnValue.toString());
        }
        ArrayList<? extends Object> dbResult = executeReadOperation(new SQLStatementBuilder()
                .select().from("QUIZZES")
                .where(columnName+"="+columnValue).toString());
        if(dbResult.size() == 1) {
            Quiz quiz = (Quiz)(dbResult.get(0));
            return quiz;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<? extends Object> executeReadOperation(String statement) {
        Pair<ResultSet, Statement> resultQuiz = getReadOperationResultSet(statement);
        try {
            ResultSet rsQuiz = resultQuiz.first();
            ArrayList<Quiz> list = new ArrayList<Quiz>();
            while(rsQuiz.next()) {
                int id = rsQuiz.getInt("ID");
                String name = rsQuiz.getString("NAME");
                String description = rsQuiz.getString("DESCRIPTION");
                int creatorID = rsQuiz.getInt("CREATOR_ID");
                PriorityQueue<QuizItem> items = new PriorityQueue<>();

                Pair<ResultSet, Statement> resultQuizItems = getReadOperationResultSet(new SQLStatementBuilder().select().from("QUIZ_ITEMS").where("QUIZ_ID=" + id).toString());
                ResultSet rsQuizItems = resultQuizItems.first();
                while(rsQuizItems.next()) {
                    int itemId = rsQuizItems.getInt("ID");
                    int quizID = rsQuizItems.getInt("QUIZ_ID");
                    int type = rsQuizItems.getInt("TYPE");
                    String question = rsQuizItems.getString("QUESTION");
                    if(type == FLASHCARD) {
                        String answer = rsQuizItems.getString("OPTION_1");
                        items.add(new Flashcard(itemId, quizID, question, answer));
                    } else if (type == MULTIPLE_CHOICE) {
                        String option1 = rsQuizItems.getString("OPTION_1");
                        String option2 = rsQuizItems.getString("OPTION_2");
                        String option3 = rsQuizItems.getString("OPTION_3");
                        String option4 = rsQuizItems.getString("OPTION_4");
                        int answer = rsQuizItems.getInt("CORRECT_ANSWER");
                        items.add(new MultipleChoice(itemId, quizID, question, option1, option2, option3, option4, answer));
                    }
                }
                resultQuizItems.second().close();

                list.add(new Quiz(id, name, description, creatorID, items, this, userManager));
            }
            resultQuiz.second().close();
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
        return executeReadOperation(new SQLStatementBuilder().select().from("QUIZZES")
                .where("CREATOR_ID="+user.getID()).toString());
    }

    /**
     * Returns all the quizzes created.
     * @return an ArrayList containing all the quizzes stored in the database.
     */
    public ArrayList<? extends Object> getAllCreatedQuizzes() {
        return executeReadOperation(new SQLStatementBuilder().select().from("QUIZZES").toString());
    }


}
