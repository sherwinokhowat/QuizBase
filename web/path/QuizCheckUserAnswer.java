package web.path;

import struct.QuizProgress;
import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import web.WebPage;

/**
 * @author Ricky Qin
 */
public class QuizCheckUserAnswer extends WebPage implements HTTPPath {

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/home");
        }

        String path = request.getPathWithoutQueryString();
        String quizName = path.substring("/quiz/".length(), path.length()-"check-answer".length()-1);
        QuizProgress progress = server.getQuizProgress(credentials.first(), quizName);
        boolean result = progress.checkUserAnswer(request.getPostBody("answer"));

        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"));
        if(result) {
            response.appendBody("Correct!");
        } else {
            response.appendBody("Wrong Answer");
        }
        return response;
    }

}
