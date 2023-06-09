package web;
import struct.User;

/**
 * Class representing the homepage of the program
 *
 * @author Sherwin Okhowat
 */
public class HomePage extends WebPage {

    public HomePage(User user) {
        setBodyAttributes("style='background-color: lightblue; overflow-x: hidden; display: flex; flex-direction: column; align-items: center; box-sizing: border-box;'");

        StringBuilder content = new StringBuilder();

        // Header section
        content.append("<div style='display: flex; justify-content: space-between; width: 100%; padding: 5px;'>");
        content.append("<img src='../images/logo.png' style='width: 150px; height: auto; margin: 0 auto;'>");
        content.append("<div style='text-align: right; font-size: 1.5em; padding-top: 35px; padding-right: 35px;'>" + user.getUsername() + "</div>");
        content.append("</div>");

        // Line
        content.append("<hr style='border: 2px solid black; width: 100%;'>");

        // Content section
        content.append("<div style='display: flex; justify-content: center; margin-bottom: 20px; width: 100%; display: flex; justify-content: center;'>");
        content.append("<a href='/home?quizzes=all' style='margin-right: 10px; background-color: lightgray; color: black; padding: 10px; text-decoration: none; border: 1px solid black;'>All Quizzes</a>");
        content.append("<a href='/home?quizzes=my' style='background-color: lightgray; color: black; padding: 10px; text-decoration: none; border: 1px solid black;'>My Quizzes</a>");
        content.append("</div>");

        appendBodyComponents(content.toString());
    }







}
