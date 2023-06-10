package web.path;

import web.HTTP;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import web.WebPage;

public class CreateQuizPage extends WebPage implements HTTPPath {
    public CreateQuizPage() {
        // you can change any part of this, i dont mind
        appendBodyComponents(
                "<form action='something' method='POST' style='display: flex; flex-direction: column; width: 300px; padding: 20px;'>",
                "<input type='text' id='quizName' name='quizName' placeholder='Quiz Name'>",
                "<textarea id='quizDescription' name='quizDescription' placeholder='Quiz Description' rows='4' cols='50'></textarea>",
                "<input type='submit' value='Add Question'>",
                "</form>"
        );
        setStyle("background-color: lightblue; overflow-x: hidden; display: flex; flex-direction: column; align-items: center; box-sizing: border-box;");
    }

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        throw new UnsupportedOperationException("Unimplemented method, 'processRequest");
    }
}