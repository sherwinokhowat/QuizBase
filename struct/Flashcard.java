package struct;

/**
 * This class represents a flashcard quiz item. Contains a question and respective answer. Contains web component
 * methods since this is a quiz item.
 * 
 * @author Sherwin Okhowat
 */
public class Flashcard extends QuizItem {
    private String question;
    private String answer;

    /**
     * Constructor for a flashcard
     * 
     * @param id the flashcard's id
     * @param frequency the frequency in which this flashcard should appear
     * @param question the question this flashcard bears
     * @param answer the answer this flashcard bears
     */
    public Flashcard(int id, int frequency, String question, String answer) {
        super(id, frequency);
        this.question = question;
        this.answer = answer;
    }

    /**
     * Returns the question that this flashcard bears
     * 
     * @return the question
     */
    public String getQuestion() {
        return this.question;
    }

    /**
     * Returns the answer that this flashcard bears
     * 
     * @return the answer 
     */
    public String getAnswer() {
        return this.answer;
    }

    /**
     * Sets the question this flashcard bears
     * 
     * @param question the new question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Sets the answer this flashcard bears
     * 
     * @param answer the new answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Converts the component to an HTML string
     * 
     * @return the HTML string representation of the component
     */
    public String toHTMLString() {
        throw new UnsupportedOperationException("Unimplemented method 'toHTMLString'");
    }

    /**
     * Returns the number of bytes
     * 
     * @return the number of bytes
     */
    public int getLength() {
        throw new UnsupportedOperationException("Unimplemented method 'getLength'");
    }
}
