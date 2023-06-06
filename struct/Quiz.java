package struct;

import java.util.PriorityQueue;
import manager.QuizManager;

public class Quiz {
    private PriorityQueue<QuizItem> quizItems;
    private int likes = -1;
    private int id;
    private String name;
    private String description;
    private User creator;
    private QuizManager manager;

    /**
     * Constructor for a quiz
     *
     * @param manager the quiz manager for this quiz
     */
    public Quiz(int id, String name, String description, int creatorId, QuizManager manager) {
        this.manager = manager;
        // unimplemented
    }

    /**
     * Returns the number of likes associated with this quiz
     *
     * @return the number of likes
     */
    public int getLikes() {
        return this.likes;
    }

    /**
     * Method for adding a like to a quiz
     *
     * @param user the user liking the quiz
     */
    public void addLike(User user) {
        this.likes++;
        throw new UnsupportedOperationException("Unimplemented method 'addLike'");
    }

    /**
     * Returns the name of the quiz
     *
     * @return the name of the quiz
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the quiz to a new name
     *
     * @param name the new name of the quiz
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the quiz
     *
     * @return the description of the quiz
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of the quiz
     *
     * @param description the new description of the quiz
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the id of the quiz
     *
     * @return the unique id of the quiz
     */
    public int getID() {
        return this.id;
    }

    /**
     * Sets the id of the quiz
     *
     * @param id the id of the quiz
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Returns the creator of the quiz
     *
     * @return the creator of the quiz
     */
    public User getCreator() {
        return this.creator;
    }

    /**
     * Adds a quiz item to the quiz
     *
     * @param item the item to be added to the quiz
     */
     public void addItem(QuizItem item) {
         this.quizItems.add(item);
        // unimplemented
     }

     /**
      * Removes a quiz item from the quiz
      *
      * @param item the item to remove from the quiz
      */
     public void removeItem(QuizItem item) {
         this.quizItems.remove(item);
     }

}
