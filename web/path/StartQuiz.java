package web.path;

import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

public class StartQuiz implements HTTPPath {

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
