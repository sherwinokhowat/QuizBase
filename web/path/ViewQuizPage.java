package web.path;

import web.*;
import struct.Quiz;
import utility.Pair;
import struct.QuizItem;
import struct.QuizProgress;

public class ViewQuizPage extends WebPage implements HTTPPath {

    public ViewQuizPage() {
        setStyle("background-color: lightblue; overflow-x: hidden; display: flex; flex-direction: column; align-items: center; box-sizing: border-box;");
    }

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/home");
        }

        String idRaw = HTTPRequest.decodeURL(request.getPathWithoutQueryString().substring("/quiz/".length()));
        QuizProgress oldProgress = server.getQuizProgress(credentials.first(), Integer.parseInt(idRaw));
        // Optional: comfirm if user wants to restart quiz since old progress will be overriden

        appendBodyComponents(new Hyperlink(idRaw+"/start", "Start Quiz", true));

        return new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"))
                .appendBody(toHTMLString());
    }

}
