package struct;

import web.WebComponent;

/**
 * Abstract class for an item in a quiz. Contains a unique id, and the frequency
 * in which this quiz item should appear.
 *
 * @author Sherwin Okhowat
 */
public abstract class QuizItem implements WebComponent {
    private int id;

    /**
     * Creates a QuizItem object. 
     * @param id The ID of the QuizItem (according to the database)
     */
    public QuizItem(int id) {
        this.id = id;
    }

    /**
     * Returns the quiz item's id
     *
     * @return the id of this quiz item
     */
    public int getId() {
        return this.id;
    }


    /**
     * Returns the HTML code representing the {@code QuizItem} object, which controls how it's displayed to the user.
     * @return a string containing the HTML code 
     */
    @Override
    public abstract String toHTMLString();
}
