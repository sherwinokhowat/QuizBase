package struct;

import manager.UserManager;

/**
 * A class that represents a User object in the program
 *
 * @author Sherwin Okhowat
 */
public class User {
    private String username;
    private int id;
    private UserManager userManager;

    /**
     * Default constructor for a user
     *
     * @param username the user's username
     * @param manager the user manager
     * @param id the unique id
     */
    public User(int id, String username, UserManager userManager) {
        this.id = id;
        this.username = username;
        this.userManager = userManager;
    }

     // ------------------------ Functionality Methods ----------------------------

    /**
     * Method to add a quiz to liked quizzes collection
     *
     * @param quiz the quiz to add
     */
    public void addLikedQuiz(Quiz quiz) {
        throw new UnsupportedOperationException("Unimplemented method 'addLikedQuiz'");
    }

    /**
     * Method to remove a quiz from the liked quizzes collection
     *
     * @param quiz the quiz to remove
     */
    public void removeLikedQuiz(Quiz quiz) {
        throw new UnsupportedOperationException("Unimplemented method 'removeLikedQuiz'");
    }

     // ------------------------ Setter Methods ----------------------------

    /**
    * Setter for the id
    *
    * @param id the id
    */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Sets the username to a new username
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

     // ------------------------ Getter Methods ----------------------------

    /**
     * Returns the username
     *
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
    * Getter for the id
    *
    * @return the id
    */
    public int getID() {
        return this.id;
    }


    // ------------------------ Database Methods ----------------------------

    /*

    we removed likes and we moved this to QuizManager
    public HashSet<Quiz> getLikedQuizzesFromDB() {
        throw new UnsupportedOperationException("Unimplemented method 'getLikedQuizzesFromDB'");
    }

    public ArrayList<Quiz> getCreatedQuizzesFromDB() {
        throw new UnsupportedOperationException("Unimplemented method 'getCreatedQuizzesFromDB'");
    }
    */

    public String toString() {
        return "ID: " + this.id + ", USERNAME: " + this.username;
    }

}
