package web.path;

import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

/**
 * A class which processes the starting of a Quiz
 *
 * @author Ricky Qin
 */
public class StartQuiz implements HTTPPath {

    /**
     * Processes the request for starting a quiz
     *
     * @param request The request
     * @param server The server
     * @return GET HTTPResponse for the next question
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/home");
        }

        String path = request.getPathWithoutQueryString();
        String idRaw = path.substring("/quiz/".length(), path.length()-"/start".length());


        server.startQuiz(credentials.first(), Integer.parseInt(idRaw));

        return new HTTPResponse().setStatus(303).setHeaderField("Location", "next-question");
    }

}
