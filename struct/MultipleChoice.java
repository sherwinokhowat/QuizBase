package struct;

import web.WebComponent;

/**
 * This class represents a multiple choice quiz item, which holds four possible
 * answers and the index of the correct answer. Contains web component
 * methods since this is a quiz item. Answer options index begins at 0 index.
 *
 * @author Sherwin Okhowat
 */
public class MultipleChoice extends QuizItem {

    private String[] answerOptions;
    private String question;
    private int correctIndex;

    /**
     * Constructs a MultipleChoice QuizItem
     *
     * @param id The ID of this MultipleChoice QuizItem
     * @param question The question
     * @param answerOne The first answer option
     * @param answerTwo The second answer option
     * @param answerThree The third answer option
     * @param answerFour The fourth answer option
     * @param correctIndex The correct index of the answer
     */
    public MultipleChoice(int id, String question, String answerOne, String answerTwo,
            String answerThree, String answerFour, int correctIndex) {
        super(id);
        this.question = question;
        this.answerOptions = new String[4];
        this.answerOptions[0] = answerOne;
        this.answerOptions[1] = answerTwo;
        this.answerOptions[2] = answerThree;
        this.answerOptions[3] = answerFour;
        this.correctIndex = correctIndex;
    }


    /**
     * Returns the correct index of the answer
     *
     * @return the index of the correct answer
     */
    public int getCorrectIndex() {
        return this.correctIndex;
    }



    /**
     * Converts the component to an HTML string
     * IMPORTANT: the caller must wrap the resulting string in a <form> tag!
     *
     * @return the HTML string representation of the component
     */
    @Override
    public String toHTMLString() {
        // Form and submit button tags will be added in their own classes, for flexibility. Spaced repetition treats it as one of its own,

        StringBuilder html = new StringBuilder("<div class='multipleChoice' id='question'>");
        html.append("<h4>" + this.question  + "</h4>"); // + ("?".equals(this.question.substring(this.question.length()-1)) ? "": "?") we might not need this, if there wasn't a "?" there probably wasn't one for a reason.
        for(int i = 0; i < this.answerOptions.length; i++) {
            html.append("<input type='radio' name='answer' value='"+(i+1)+"' required>");
            html.append(this.answerOptions[i]);
            html.append("<br>");
        }
        html.append("<input type='submit' value = 'Submit'>");
        return html.toString();
    }

    /**
     * Cannot set style of MultipleChoice as it is already determined. This method does not do anything.
     *
     * @param style The Style
     * @return This MultipleChoice QuizItem
     */
    public WebComponent setStyle(String style) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStyle'");
    }

}
