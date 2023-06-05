package struct;

import java.util.Set;

import manager.UserManager;

import java.util.HashSet;

/**
 * A class that represents a User object in the program
 *
 * @author Sherwin Okhowat
 */
public class User {
    private String username;
    private int id;
    private UserManager manager;
    private Set<Quiz> likedQuizzes;

    /**
     * Default constructor for a user
     *
     * @param username the user's username
     */
    public User(String username) {
        throw new UnsupportedOperationException("Unimplemented constructor 'User'");
    }

     // ------------------------ Functionality Methods ----------------------------

    /**
     * Method to add a quiz to liked quizzes collection
     *
     * @param quiz the quiz to add
     */
    public void addLikedQuiz(Quiz quiz) {
        this.likedQuizzes.add(quiz);
    }

    /**
     * Method to remove a quiz from the liked quizzes collection
     *
     * @param quiz the quiz to remove
     */
    public void removeLikedQuiz(Quiz quiz) {
        this.likedQuizzes.remove(quiz);
    }

     // ------------------------ Setter Methods ----------------------------

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
     * Getter for the collection of liked quizzes
     *
     * @return the collection of liked quizzes
     */
    public Set<Quiz> getLikedQuizzes() {
        return this.likedQuizzes;
    }

    // ------------------------ Database Methods ----------------------------

    /**
    * Getter for the id
    *
    * @return the id
    */
    public int getID() {
        return this.id;
    }

    /**
    * Setter for the id
    *
    * @param id the id
    */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Retrieves the liked quizzes from the database
     *
     * @return whether the retrieval was successful or not
     */
    private boolean getLikedQuizzesFromDB() {
        throw new UnsupportedOperationException("Unimplemented constructor 'getLikedQuizzesFromDB'");
    }


}
