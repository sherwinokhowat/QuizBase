package web.path;

import struct.QuizProgress;
import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Hyperlink;
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
        int quizID = Integer.parseInt(path.substring("/quiz/".length(), path.length()-"check-answer".length()-1));
        QuizProgress progress = server.getQuizProgress(credentials.first(), quizID);
        int result = progress.checkUserAnswer(request.getPostBody("answer"));

        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"));
        if(result == 1) {
            // the quiz item that we were testing.decreaseFrequency();
            response.appendBody("Correct!");
        } else if(result == 0) {
            // the quiz item that we were testing.increaseFrequency();
            response.appendBody("Wrong Answer");
        } else {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/quiz/"+quizID+"/next-question");
        }
        response.appendBody("<br>");
        response.appendBody(new Hyperlink("/quiz/"+quizID+"/next-question", "Next Question", true).toHTMLString());
        return response;
    }

}
