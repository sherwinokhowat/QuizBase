package web.path;

import struct.QuizProgress;
import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Hyperlink;
import web.Server;
import web.WebPage;

/**
 * Class which implements the functionality for checking the User's answer
 *
 * @author Ricky Qin
 */
public class QuizCheckUserAnswer extends WebPage implements HTTPPath {

    /**
     * Essentially checks whether the answer was right or wrong and handles it accordingly.
     *
     * @param request The request
     * @param server The server
     * @return The appropriate HTTPResponse
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
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
        response.appendBody("<html><head><meta http-equiv='refresh' content='3;url=/quiz/" + quizID+ "/next-question' /></head><body><p>Redirecting you to the next question... </p></body></html>");

//        response.appendBody(new Hyperlink("/quiz/"+quizID+"/next-question", "Next Question", true).toHTMLString());
        return response;
    }

}
