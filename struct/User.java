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

    /**
     * Constructs a User object
     *
     * @param id the unique id (according to the {@code USERS} database)
     * @param username the user's username
     */
    public User(int id, String username) {
        this.id = id;
        this.username = username;
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
