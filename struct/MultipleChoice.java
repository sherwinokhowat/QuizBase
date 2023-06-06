package struct;

/**
 * This class represents a multiple choice quiz item, which holds four possible
 * answers and the index of the correct answer. Contains web component
 * methods since this is a quiz item.
 * 
 * @author Sherwin Okhowat
 */
public class MultipleChoice extends QuizItem {
    private String[] answerOptions;
    private String question;
    private int correctIndex;

    /**
     * Constructor for a multiple choice quiz item
     * 
     * @param id           the multiple choice question's id
     * @param frequency    the frequency in which this multiple choice question
     *                     should appear
     * @param question     the multiple choice question string
     * @param answerTwo    the second answer option
     * @param answerThree  the third answer option
     * @param answerFour   the fourth answer option
     * @param correctIndex the correct index of the answer in the array
     */
    public MultipleChoice(int id, double frequency, String question, String answerOne, String answerTwo,
            String answerThree,
            String answerFour, int correctIndex) {
        super(id, frequency);
        this.question = question;
        this.answerOptions = new String[4];
        this.answerOptions[0] = answerOne;
        this.answerOptions[1] = answerTwo;
        this.answerOptions[2] = answerThree;
        this.answerOptions[3] = answerFour;
        this.correctIndex = correctIndex;
    }

    /**
     * Sets the correct index of the answer
     * 
     * @param index the correct index of the answer
     */
    public void setCorrectIndex(int index) {
        if (index < 4 && index >= 0) {
            this.correctIndex = index;
        }
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
     * Returns the array of answer options
     * 
     * @return the array of answer options
     */
    public String[] getAnswerOptions() {
        return this.answerOptions;
    }

    /**
     * Converts the component to an HTML string
     * 
     * @return the HTML string representation of the component
     */
    @Override
    public String toHTMLString() {
        StringBuilder html = new StringBuilder("<div class='multipleChoice'>"); //open1
        html.append("<form id='multipleChoice'>");
        html.append("<div class='question'>"); // open2
        html.append("<p>" + this.question + ("?".equals(this.question.substring(this.question.length()-1)) ? "": "?") + "</p>");
        html.append("div class = 'answerOptions'>"); //open3
        for(int i = 0; i < this.answerOptions.length; i++) {
            html.append("<label> <input type='radio' name='question" + i + "' value='"+ (char)(i+65) + "'>");
            html.append(this.answerOptions[i]);
            html.append("</label>");
        }
        
        html.append("</div>"); // close3
        html.append("</div>"); // close2
        html.append("<button type='submit' class='submit-button'>Submit</button>");
        html.append("</form>");
        html.append("</div>"); //close1

        // return html.toString();

        throw new UnsupportedOperationException("Unimplemented method 'toHTMLString'");
    }

    /**
     * Returns the number of bytes
     * 
     * @return the number of bytes
     */
    @Override
    public int getLength() {
        throw new UnsupportedOperationException("Unimplemented method 'getLength'");
    }

}
