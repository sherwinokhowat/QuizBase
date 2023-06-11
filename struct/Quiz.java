package struct;

import java.util.ArrayList;

import manager.QuizManager;
import manager.UserManager;
import utility.SQLStatementBuilder;
import web.Hyperlink;
import web.WebComponent;

public class Quiz implements WebComponent {


    private int id;
    private String name;
    private String description;
    private int creatorId;
    // private int likes = -1;
    private QuizManager quizManager;
    private UserManager userManager;

    /**
     * Constructor for a quiz
     *
     * @param manager the quiz manager for this quiz
     * @param items
     */
    public Quiz(int id, String name, String description, int creatorId, QuizManager quizManager, UserManager userManager) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.quizManager = quizManager;
        this.userManager = userManager;
    }

    /**
     * Returns the number of likes associated with this quiz
     *
     * @return the number of likes
     */
    // public int getLikes() {
    //     return this.likes;
    // }

    /**
     * Method for adding a like to a quiz
     *
     * @param user the user liking the quiz
     */
    // public void addLike(User user) {
    //     this.likes++;
    //     throw new UnsupportedOperationException("Unimplemented method 'addLike'");
    // }

    /**
     * Returns the id of the quiz
     *
     * @return the unique id of the quiz
     */
    public int getID() {
        return this.id;
    }

    /**
     * Returns the name of the quiz
     *
     * @return the name of the quiz
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the quiz to a new name
     *
     * @param name the new name of the quiz
     */
    // public void setName(String name) {
    //     this.name = name;
    // }

    /**
     * Returns the description of the quiz
     *
     * @return the description of the quiz
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of the quiz
     *
     * @param description the new description of the quiz
     */
    // public void setDescription(String description) {
    //     this.description = description;
    // }

    /**
     * Returns the user id of the creator of the quiz
     *
     * @return the user id
     */
    public int getCreatorId() {
        return this.creatorId;
    }

    /**
     * Gets the quiz items in this quiz
     *
     * @return An arraylist of QuizItems
     */
    public ArrayList<QuizItem> getQuizItems() {
        return quizManager.executeReadQuizItemOperation(new SQLStatementBuilder().select()
                .from("QUIZ_ITEMS").where("QUIZ_ID="+id).toString());
    }

    /**
     * Returns the HTML form of a quiz (when being viewed in a list.)
     */
     @Override
     public String toHTMLString() {
         final int MAX_DESC_LENGTH = 110;
         StringBuilder html = new StringBuilder();

         html.append("<div style='border: 1px solid black; padding: 10px; margin: 13px; width: 140px; height: 90px; background-color: white; font-size: 0.8em; display: flex; flex-direction: column; justify-content: space-between;'>");
         html.append("<h1 style='margin: 0; font-size: 1.2em; flex-grow: 0;'>");
         // remove this.id if duplicates allowed
         html.append(new Hyperlink("/quiz/"+this.getID(), this.name, false).toHTMLString());
         html.append("</h1>");
         html.append("<h2 style='margin: 0; color: gray; font-size: 1em; flex-grow: 0;'>Created by: ");
         html.append(((User)userManager.getBy("ID", this.creatorId)).getUsername());
         html.append("</h2>");

         String shortenedDescription = this.description.length() > MAX_DESC_LENGTH ?
                 this.description.substring(0, MAX_DESC_LENGTH) + "..." :
                 this.description;

         html.append("<p style='margin: 0; font-size: 0.8em; flex-grow: 1;'>");
         html.append(shortenedDescription);
         html.append("</p>");
         html.append("</div>");

         return html.toString();
     }

    /**
     * Cannot set style of quiz as it is already determined. This method does not do anything.
     *
     * @param style A string
     * @return This Quiz
     */
    @Override
    public WebComponent setStyle(String style) {
        return this;
    }
}
