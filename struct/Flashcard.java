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
     * Constructs a Flashcard
     *
     * @param id The ID of the QuizItem
     * @param question The question
     * @param answer The answer
     */
    public Flashcard(int id, String question, String answer) {
        super(id);
        this.question = question;
        this.answer = answer;
    }


    /**
     * Converts the component to an HTML string
     *
     * @return the HTML string representation of the component
     */
    public String toHTMLString() {
        StringBuilder html = new StringBuilder("<div class='flashcard' id='question'>");
        html.append("<h4>" + this.question  + "</h4>"); // + ("?".equals(this.question.substring(this.question.length()-1)) ? "": "?") we might not need this, if there wasn't a "?" there probably wasn't one for a reason.
        html.append("<br>");
        html.append("<button type='button' onclick='showAnswer()'>Show Answer</button>");
        html.append("<br>");
        html.append("<div class='hidden-content' style='display:none' id='solution'>");
        html.append("<h4>" + this.answer + "</h4>");
        html.append("<input type='radio' name='answer' value='correct' required>Got Correct Answer<br>");
        html.append("<input type='radio' name='answer' value='incorrect'>Got Incorrect Answer<br>");
        html.append("<input type='submit' value='Submit'>");

        /*
         * So I'll work on this later:
         * On spaced repetition, after the answer is revealed user must select whether it's correct or incorrect
         * On a classic quiz, the user has to type their answer, though having flashcards in a 'classic quiz' defeats the purpose
         */
        html.append("</div>");
        html.append("</div>");
        return html.toString();
        // so the user will have to press "Show Answer", which will show the answer, as well as a 'Incorrect'/'Correct' slider
    }

    /**
     * Cannot set style of Flashcard as it is already determined. This method does not do anything.
     *
     * @param style The Style
     * @return This Flashcard
     */
    @Override
    public WebComponent setStyle(String style) {
        return this;
    }
}
