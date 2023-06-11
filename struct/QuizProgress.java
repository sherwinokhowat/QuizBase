package struct;

import java.util.ArrayList;

import web.Server;

/**
 * Stores a user's progress on the quiz and determines the order of the quizitems
 *
 * @author Ricky Qin
 */
public class QuizProgress {

    private String quizName;
    private Server server;
    private ArrayList<Integer> quizItemIDS;
    private int[] probabilities;

    /**
     * Constructs a QuizProgress object for the provided quiz
     *
     * @param quizName The name of the quiz
     */
    public QuizProgress(String quizName, Server server) {
        this.quizName = quizName;
        this.server = server;
        Quiz quiz = (Quiz)server.getQuizManager().getBy("NAME", quizName);
        this.quizItemIDS = server.getQuizManager().getQuizItemIDS(quiz.getID());
    }

    /**
     * Gets the next QuizItem to send to the client
     */
    public QuizItem getNextQuizItem() {
        return null;
    }
}
