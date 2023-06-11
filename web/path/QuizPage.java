package web.path;

import web.*;
import struct.Quiz;
import utility.Pair;
import struct.QuizItem;

public class QuizPage extends WebPage implements HTTPPath {

    public QuizPage() {
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
//            System.out.println("quiz: " + quiz);
//            System.out.println("username: " + username);
//            System.out.println("id: " + id);
//            System.out.println("quizName: " + quizName);
        }

        QuizItem item = quiz.getNextItem();
        if(item == null) {
            appendBodyComponents("<p>No more questions!!</p>");
        } else {
            appendBodyComponents(item.toHTMLString());
        }

        return new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"))
                .appendBody(toHTMLString());
    }

}
