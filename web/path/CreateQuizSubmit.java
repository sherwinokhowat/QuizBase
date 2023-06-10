package web.path;

import struct.Quiz;
import struct.User;
import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

/**
 * @author Ricky Qin
 */
public class CreateQuizSubmit implements HTTPPath {

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
        }

        String name = request.getPostBody("quizName");
        String description = request.getPostBody("quizDescription");
        User user = (User)(server.getUserManager().getBy("USERNAME", "'"+credentials.first()+"'"));
        Quiz quiz = (Quiz)(server.getQuizManager().addQuiz(user.getID(), name, description));

        int questionNum = 1;
        while(true) {
            String question = request.getPostBody("question"+questionNum);
            if(question == null) {
                break;
            }
            String answer = request.getPostBody("answer"+questionNum);

            String[] options = new String[4];
            int correctAnswer = -1;
            if(answer == null) {
                options[0] = request.getPostBody("optionA"+questionNum);
                options[1] = request.getPostBody("optionB"+questionNum);
                options[2] = request.getPostBody("optionC"+questionNum);
                options[3] = request.getPostBody("optionD"+questionNum);
                correctAnswer = Integer.parseInt(request.getPostBody("correctAnswer"+questionNum));
                if(options[0] == null || options[1] == null || options[2] == null || options[3] == null || correctAnswer == -1) {
                    return new HTTPResponse().setStatus(400);
                } else {
                    server.getQuizManager().addMultipleChoice(quiz.getID(), question, options[0], options[1], options[2], options[3], correctAnswer);
                }
            } else {
                server.getQuizManager().addFlashCard(quiz.getID(), question, answer);
            }
            questionNum++;
        }
        if(questionNum == 1) {
            return new HTTPResponse().setStatus(400);
        }
        return new HTTPResponse().setStatus(303).setHeaderField("Location", "../../home");
    }
}
