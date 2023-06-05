package struct;

import java.util.Set;

import manager.UserManager;

import java.util.HashSet;
import manager.UserManager;

/**
 * A class that represents a User object in the program
 *
 * @author Sherwin Okhowat
 */
public class User {
    private String username;
    private int id;
    private UserManager manager;

    /**
     * Default constructor for a user
     *
     * @param username the user's username
     * @param manager the user manager
     * @param id the unique id
     */
    public User(String username, UserManager manager, int id) {
        this.username = username;
        this.id = id;
        this.manager = manager;
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
    
    /**
     * Retrieves the liked quizzes from the database
     *
     * @return whether the retrieval was successful or not
     */
    private boolean getLikedQuizzesFromDB() {
        throw new UnsupportedOperationException("Unimplemented constructor 'getLikedQuizzesFromDB'");
    }


}
