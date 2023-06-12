package web.path;

import struct.Flashcard;
import struct.MultipleChoice;
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
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
        }
        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"));

        addHeader(request, server);

        String path = request.getPathWithoutQueryString();
        int quizID = Integer.parseInt(path.substring("/quiz/".length(), path.length()-"next-question".length()-1));
        QuizProgress progress = server.getQuizProgress(credentials.first(), quizID);

        System.out.println("QuizGetNextQuestion.java: quiz item fetched.");


        // adding an if statement which only does that in response to a post request.

        // appendHeadComponents("<script src='/js/flashcard.js'></script>");
        QuizItem quizItem = progress.getNextQuizItem();
        if(quizItem != null) {
            appendBodyComponents("<form action='/quiz/"+quizID+"/check-answer' method='POST'>", quizItem.toHTMLString(), "</form>");
            if(quizItem instanceof Flashcard) {
                appendBodyComponents("<script>",
                        "function showAnswer() {\r\n" + //
                        "    document.getElementById(\"solution\").style.display = \"block\";\r\n" + //
                        "}",
                        "</script>");
            }
            response.appendBody(toHTMLString());

        } else {
            response.appendBody("No more questions left!");
            server.endQuiz(credentials.first(), quizID);
        }

        return response;
    }

}
