package web.path;
import java.util.ArrayList;

import struct.Quiz;
import struct.User;
import utility.Pair;
import web.HTTP;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import web.WebPage;

/**
 * Class representing the homepage of the program
 *
 * @author Sherwin Okhowat
 */
public class HomePage extends WebPage implements HTTPPath {

    private boolean displayAll;

    public HomePage(boolean displayAll) {
        setBodyAttributes("style='background-color: lightblue; overflow-x: hidden; display: flex; flex-direction: column; align-items: center; box-sizing: border-box;'");
        this.displayAll = displayAll;
    }

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/");
        } else {
            HTTPResponse response = new HTTPResponse().setStatus(200)
                    .setHeaderField("Content-Type", HTTP.contentType("html"));
            User user = server.getUserManager().authenticateUser(
                    credentials.first(), credentials.second());

            StringBuilder content = new StringBuilder();
            // Header section
            content.append("<div style='display: flex; justify-content: space-between; width: 100%; padding: 20px;'>");
            content.append("<img src='../images/logo.png' style='width: 150px; height: auto;'>");
            content.append("<div style='text-align: right; font-size: 1.5em; padding-top: 35px; padding-right: 35px;'>" + user.getUsername() + "</div>");
            content.append("<a href='/signout' style='text-align: right; font-size: 1.5em;'> Sign out </a>");
            content.append("</div>");

            // Line
            content.append("<hr style='border: 2px solid black; width: 100%;'>");

            // Content section
            content.append("<div style='display: flex; justify-content: center; margin-bottom: 20px; width: 100%; display: flex; justify-content: center;'>");
            content.append("<a href='/home?quizzes=all' style='margin-right: 10px; background-color: " + (displayAll ? "#FFCCCB" : "#F2F2F2") + "; color: black; padding: 10px; text-decoration: none; border: 1px solid black;'>All Quizzes</a>");
            content.append("<a href='/home?quizzes=my' style='background-color:" + (!displayAll ? "#FFCCCB" : "#F2F2F2") + "; color: black; padding: 10px; text-decoration: none; border: 1px solid black;'>My Quizzes</a>");
            content.append("</div>");

            appendBodyComponents(content.toString());

            String query = request.getQueryString();
            if("quizzes=my".equals(query)) { // display user's quizzes
                ArrayList<? extends Object> quizzes = server.getQuizManager().getUserCreatedQuizzes(user);
                for(Object quiz : quizzes) {
                    appendBodyComponents(((Quiz) quiz).toHTMLString());
                }
            } else { // display all quizzes
                ArrayList<? extends Object> quizzes = server.getQuizManager().getAllCreatedQuizzes();
                for(Object quiz : quizzes) {
                    appendBodyComponents(((Quiz) quiz).toHTMLString());
                }
            }
            response.appendBody(toHTMLString());
            return response;
        }
    }








}
