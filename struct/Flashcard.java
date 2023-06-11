package struct;

import web.WebComponent;

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
    public Flashcard(int id, int quizID, String question, String answer) {
        super(id, quizID);
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
     * Converts the component to an HTML string
     *
     * @return the HTML string representation of the component
     */
    public String toHTMLString() {
        return "<h4>" + question + "</h4>";
        // so the user will have to press "Show Answer", which will show the answer, as well as a 'Incorrect'/'Correct' slider
    }

    @Override
    public WebComponent setStyle(String style) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStyle'");
    }
}
