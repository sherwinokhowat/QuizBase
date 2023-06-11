package web.path;

import struct.QuizItem;
import struct.QuizProgress;
import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import web.WebPage;

/**
 * @author Ricky Qin
 */
public class QuizGetNextQuestion extends WebPage implements HTTPPath {

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/home");
        }

        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"));

        String path = request.getPathWithoutQueryString();
        int quizID = Integer.parseInt(path.substring("/quiz/".length(), path.length()-"next-question".length()-1));
        QuizProgress progress = server.getQuizProgress(credentials.first(), quizID);
        String itemHTML = progress.getNextQuizItem().toHTMLString();

        response.appendBody("<form action='/quiz/"+quizID+"/check-answer' method='POST'")
                .appendBody(itemHTML).appendBody("</form>");
        return response;
    }

}
