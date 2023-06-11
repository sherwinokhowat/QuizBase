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

    @Override
    public abstract String toHTMLString();
}
