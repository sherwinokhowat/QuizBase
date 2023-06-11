package struct;

import java.util.ArrayList;
import java.util.Arrays;

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

    /**
     * The probability of choosing the ith item is {@code prob[i] / (sum of prob)}
     */
    private int[] prob;

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

        prob = new int[quizItemIDS.size()];
        Arrays.fill(prob, 1);
    }

    /**
     * Gets the next QuizItem to send to the client
     *
     * @return The next quiz item
     */
    public QuizItem getNextQuizItem() {
        int sumProb = 0;
        for(int i = 0; i < prob.length; i++) {
            sumProb += prob[i];
        }
        int randInt = (int)(Math.random()*sumProb)+1;
        int idx = 0;
        int currSum = 1;
        while(!((currSum <= randInt) && (randInt < currSum+prob[idx]))) {
            idx++;
        }
        return server.getQuizManager().getQuizItemBy("ID", quizItemIDS.get(idx));
    }

    public void checkUserAnswer() {

    }
}
