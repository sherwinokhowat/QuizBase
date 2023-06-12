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
     * @param id the unique id
     * @param username the user's username
     * @param manager the user manager
     */
    public User(int id, String username, UserManager userManager) {
        this.id = id;
        this.username = username;
        this.userManager = userManager;
    }

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
}
