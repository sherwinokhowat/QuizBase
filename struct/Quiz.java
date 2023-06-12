package struct;

import java.util.ArrayList;

import manager.QuizManager;
import manager.UserManager;
import utility.SQLStatementBuilder;
import web.Hyperlink;
import web.WebComponent;

/**
 * A class representing a Quiz object.
 *
 * @author Sherwin Okhowat
 */
public class Quiz implements WebComponent {


    private int id;
    private String name;
    private String description;
    private int creatorId;
    private QuizManager quizManager;
    private UserManager userManager;

    /**
     * Constructs a Quiz object
     *
     * @param id The ID of this Quiz
     * @param name The name of this Quiz
     * @param description The description of this Quiz
     * @param creatorId The ID of the creator of this Quiz
     * @param quizManager An instance of a QuizManager used to manage this quiz
     * @param userManager An instance of a UserManager used to manage the creator
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
     * Converts the Quiz to an HTML string. This code shows how a quiz is represented on the home page.
     *
     * @return the HTML string representation of the component
     */
     @Override
     public String toHTMLString() {
         final int MAX_DESC_LENGTH = 110;
         StringBuilder html = new StringBuilder();

         html.append("<div style='border: 1px solid black; padding: 10px; margin: 13px; width: 153px; height: 150px; background-color: white; font-size: 12.8px; display: flex; flex-direction: column; justify-content: space-between;'>");
         html.append("<h1 style='margin: 0; font-size: 19.2px; flex-grow: 0;'>");
         // remove this.id if duplicates allowed
         // html.append(new Hyperlink("/quiz/"+this.name+"?"+this.id, this.name, false).toHTMLString());
         html.append(new Hyperlink("/quiz/"+this.getID(), this.name, false).toHTMLString());
         html.append("</h1>");
         html.append("<h2 style='margin: 0; color: gray; font-size: 16px; flex-grow: 0;'>Created by: ");
         html.append(userManager.getBy("ID", this.creatorId).getUsername());
         html.append("</h2>");

         String shortenedDescription = this.description.length() > MAX_DESC_LENGTH ?
                 this.description.substring(0, MAX_DESC_LENGTH) + "..." :
                 this.description;

         html.append("<p style='margin: 0; font-size: 12.8px; flex-grow: 1;'>");
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
