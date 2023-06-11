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
    private int quizID;

    public QuizItem(int id, int quizID) {
        this.id = id;
        this.quizID = quizID;
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
     * Returns the id of the quiz this quiz item is in
     *
     * @return the id
     */
    public int getQuizId() {
        return this.quizID;
    }

    @Override
    public abstract String toHTMLString();
}
