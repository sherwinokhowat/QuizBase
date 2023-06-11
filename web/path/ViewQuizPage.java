package web.path;

import web.*;
import struct.Quiz;
import utility.Pair;
import struct.QuizItem;

public class ViewQuizPage extends WebPage implements HTTPPath {

    public ViewQuizPage() {
        setStyle("background-color: lightblue; overflow-x: hidden; display: flex; flex-direction: column; align-items: center; box-sizing: border-box;");
    }

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        String quizName = HTTPRequest.decodeURL(request.getPathWithoutQueryString().substring("/quiz/".length()));
        String id = request.getQueryString();
        String username = server.checkSessionID(request).first();
        Quiz quiz = server.getActiveQuiz(username);

        if(quiz == null) {
            server.startActiveQuiz(new Pair<>(username, server.getQuizManager().getQuiz(Integer.parseInt(id), quizName)));
            quiz = server.getActiveQuiz(username);
        }

        QuizItem item = quiz.getNextItem();
        if(item == null) {
            appendBodyComponents("<p>This quiz has no questions!</p>");
        } else {
            appendBodyComponents(item.toHTMLString());
            // here we should be increasing/decreasing the frequency based on
            // whether the user got the question right or wrong
            quiz.addItem(item);
        }


        return new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"))
                .appendBody(toHTMLString());
    }

}
