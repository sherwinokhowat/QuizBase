package web;
import struct.User;

/**
 * Class representing the homepage of the program
 *
 * @author Sherwin Okhowat
 */
public class HomePage extends WebPage {

    public HomePage(User user) {
        // I WILL MAKE THIS ALL INTO ONE METHOD LATER ITS JUST LIKE THIS FOR NOW FOR READABILITY
        // DO NOT CHANGE THE FORMATTING. IT IS LIKE THIS ON PURPOSE CURRENTLY FOR READABILITY
        setBodyAttributes("style='background-color: lightblue; display: flex; flex-direction: column; justify-content: center; align-items: center;'");

        StringBuilder content = new StringBuilder();
        content.append("<div style='display: flex; justify-content: space-between; width: 100%; padding: 20px;'>");
        content.append("<img src='../images/logo.png' style='width: 100px; height: auto;'>");
        content.append("<div style='text-align: right; font-size: 1.5em; padding-top: 35px; padding-right: 35;'>" + user.getUsername() + "</div>");
        content.append("</div>");

        content.append("<hr style='border: 2px solid black; width: 150%;'>");

        content.append("<div style='display: flex; justify-content: center; margin-bottom: 20px;'>");
        content.append("<a href='/all-quizzes' style='margin-right: 10px; background-color: lightgray; color: black; padding: 10px; text-decoration: none; border: 1px solid black;'>All Quizzes</a>");
        content.append("<a href='/my-quizzes' style='background-color: lightgray; color: black; padding: 10px; text-decoration: none; border: 1px solid black;'>My Quizzes</a>");
        content.append("</div>");

        appendBodyComponents(content.toString());
    }
}
