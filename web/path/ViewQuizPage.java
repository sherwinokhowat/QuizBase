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
        // returns the id: 3, 5, 7...
        String id = request.getQueryString();
        String username = server.checkSessionID(request).first();

        QuizProgress oldProgress = server.getQuizProgress(credentials.first(), Integer.parseInt(idRaw));
        // comfirm if user wants to restart quiz since old progress will be overriden

        server.startQuiz(username, Integer.parseInt(idRaw));

        
        /* QuizItem item = oldProgress.getNextQuizItem();
        if(item == null) {
            appendBodyComponents("<p>This quiz has no questions!</p>");
        } else {
             appendBodyComponents(item.toHTMLString());
            // here we should be increasing/decreasing the frequency based on
            // whether the user got the question right or wrong
           //  quiz.addItem(item);
        } */
        
        // Optional: comfirm if user wants to restart quiz since old progress will be overriden

        appendBodyComponents(new Hyperlink(idRaw+"/start", "Start Quiz", true));

        return new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"))
                .appendBody(toHTMLString());
    }

}
