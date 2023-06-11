package web.path;

import struct.QuizProgress;
import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import web.WebPage;

public class QuizGetNextQuestion extends WebPage implements HTTPPath {

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/home");
        }

        String path = request.getPathWithoutQueryString();
        String quizName = path.substring("/quiz/".length(), path.length()-"next-question".length()-1);
        QuizProgress progress = server.getQuizProgress(credentials.first(), quizName);

        return new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"))
                .appendBody(progress.getNextQuizItem().toHTMLString());
    }

}
