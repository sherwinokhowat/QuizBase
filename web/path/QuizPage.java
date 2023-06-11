package web.path;

import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import web.WebPage;

public class QuizPage extends WebPage implements HTTPPath {

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        String quizName = request.getPathWithoutQueryString().substring("/quiz/".length());
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processRequest'");
    }

}
