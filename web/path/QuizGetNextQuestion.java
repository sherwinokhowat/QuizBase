package web.path;

import struct.QuizItem;
import struct.QuizProgress;
import struct.User;
import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import web.WebPage;

/**
 * @author Ricky Qin
 */
public class QuizGetNextQuestion extends WebPage implements HTTPPath {

    public QuizGetNextQuestion() {
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
        }
        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"));
        User user = server.getUserManager().authenticateUser(
                credentials.first(), credentials.second());

        String buttonStyle1 = "margin-right: 10px; background-color: " + (displayAll ? "#FFCCCB" : "#F2F2F2") + "; color: black; padding: 10px; text-decoration: none; border: 1px solid black;";
        String buttonStyle2 = "background-color:" + (!displayAll ? "#FFCCCB" : "#F2F2F2") + "; color: black; padding: 10px; text-decoration: none; border: 1px solid black;";

        addHeader(request, server);

        String path = request.getPathWithoutQueryString();
        int quizID = Integer.parseInt(path.substring("/quiz/".length(), path.length()-"next-question".length()-1));
        QuizProgress progress = server.getQuizProgress(credentials.first(), quizID);
        String itemHTML = progress.getNextQuizItem().toHTMLString();
        System.out.println("QuizGetNextQuestion.java: quiz item fetched.");


        // adding an if statement which only does that in response to a post request. 

        appendHeadComponents("<script src='/js/flashcard.js'></script>");
        
        appendBodyComponents("<form action='/quiz/"+quizID+"/check-answer' method='POST'", itemHTML, "</form>");

        return response;
    }

}
