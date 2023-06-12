package web.path;

import web.*;
import utility.Pair;

/**
 * A class which represents the viewing of a quiz page
 *
 * @author Ricky Qin
 */
public class ViewQuizPage extends WebPage implements HTTPPath {

    public ViewQuizPage() {
        setStyle("background-color: lightblue; overflow-x: hidden; display: flex; flex-direction: column; align-items: center; box-sizing: border-box;");
    }

    /**
     * Processes the request for viewing a quiz page
     *
     * @param request The request
     * @param server The server
     * @return GET HTTPResponse
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/home");
        }

        addHeader(request, server);

        int quizID = Integer.parseInt(request.getPathWithoutQueryString().substring("/quiz/".length()));

        // Optional: confirm if user wants to restart quiz since old progress will be overriden
        // QuizProgress oldProgress = server.getQuizProgress(credentials.first(), Integer.parseInt(idRaw));

        int numItems = server.getQuizManager().getQuizItemIDS(quizID).size();
        appendBodyComponents("Number of items: "+numItems, "<br>");
        appendBodyComponents(new Hyperlink(quizID+"/start", "Start Quiz", true));

        return new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"))
                .appendBody(toHTMLString());
    }

}
