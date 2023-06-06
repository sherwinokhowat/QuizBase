package struct;

/**
 * Abstract class for an item in a quiz. Contains a unique id, and the frequency
 * in which this quiz item should appear.
 * 
 * @author Sherwin Okhowat
 */
public abstract class QuizItem implements Comparable<QuizItem>, WebComponent {
    private int id;
    private int frequency;

    public QuizItem(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
    }

    /**
     * Increasing the frequency of this quiz item. If the user gets this quiz item
     * wrong, it will increase the frequency in which this quiz item appears
     */
    public void increaseFrequency() {
        this.frequency++;
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
     * Sets the quiz item's id
     * 
     * @param id the quiz item's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns this quiz item's frequency
     * 
     * @return this quiz item's frequency
     */
    public int getFrequency() {
        return this.frequency;
    }

    /**
     * Method that defines the compare method as one which compares the frequencies
     * 
     * @param other the other quiz item being compared
     * @return the order
     */
    @Override
    public int compareTo(QuizItem other) {
        if (this.getFrequency() < other.getFrequency()) {
            return 1;
        } else if (this.getFrequency() > other.getFrequency()) {
            return -1;
        }
        return 0;
    }

}
