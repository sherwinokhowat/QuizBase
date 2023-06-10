package web.path;
import java.util.ArrayList;

import struct.Quiz;
import struct.User;
import utility.Pair;
import web.HTTP;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Hyperlink;
import web.Server;
import web.WebPage;

/**
 * Class representing the homepage of the program
 *
 * @author Sherwin Okhowat
 */
public class HomePage extends WebPage implements HTTPPath {

    public HomePage() {
        setStyle("background-color: lightblue; overflow-x: hidden; display: flex; flex-direction: column; align-items: center; box-sizing: border-box;");
    }

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        boolean displayAll;
        if("quizzes=my".equals(request.getQueryString())) {
            displayAll = false;
        } else {
            displayAll = true;
        }
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
        } else {
            HTTPResponse response = new HTTPResponse().setStatus(200)
                    .setHeaderField("Content-Type", HTTP.contentType("html"));
            User user = server.getUserManager().authenticateUser(
                    credentials.first(), credentials.second());

            String buttonStyle1 = "margin-right: 10px; background-color: " + (displayAll ? "#FFCCCB" : "#F2F2F2") + "; color: black; padding: 10px; text-decoration: none; border: 1px solid black;";
            String buttonStyle2 = "background-color:" + (!displayAll ? "#FFCCCB" : "#F2F2F2") + "; color: black; padding: 10px; text-decoration: none; border: 1px solid black;";
            String signOutButtonStyle = "background-color: #F2F2F2; color: black; padding: 10px; text-decoration: none; border: 1px solid black;";


            // Header section
            appendBodyComponents("<div style='display: flex; justify-content: space-between; width: 100%; padding: 20px;'>",
                    "<img src='../images/logo.png' style='width: 150px; height: auto;'>",
                    new Hyperlink("/create-quiz", "Create A Quiz", false), // ideally I would put this in a div so that an image could be next to the hyperlink, or just have this as an image
                    "<div style='text-align: right; font-size: 1.5em; padding-top: 35px; padding-right: 35px;'>" + user.getUsername() + "</div>",
                    new Hyperlink("/signout", "Sign Out", false).setStyle(signOutButtonStyle),
                    "</div>");

            // Line
            appendBodyComponents("<hr style='border: 2px solid black; width: 100%;'>");

            // Content section
            appendBodyComponents("<div style='display: flex; justify-content: center; margin-bottom: 20px; width: 100%; display: flex; justify-content: center;'>",
                    new Hyperlink("/home?quizzes=all", "All Quizzes", false).setStyle(buttonStyle1),
                    new Hyperlink("/home?quizzes=my", "My Quizzes", false).setStyle(buttonStyle2));

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
