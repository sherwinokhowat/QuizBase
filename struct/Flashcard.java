package struct;

public class Flashcard extends QuizItem {
    private String question;
    private String answer;

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
}
