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
     * The index of the current quiz item (the one that was most recently sent)
     */
    private int currQuizItem = -1;

    /**
     * The probability of choosing the ith item is {@code prob[i] / (sum of prob)}
     */
    private int[] prob;

    /**
     * Constructs a QuizProgress object for the provided quiz
     *
     * @param quizID The id of the quiz
     */
    public QuizProgress(int quizID, Server server) {
        this.server = server;
        this.quizItemIDS = server.getQuizManager().getQuizItemIDS(quizID);
        System.out.println("QuizProgress.java: " + quizItemIDS.size());
        prob = new int[quizItemIDS.size()];
        Arrays.fill(prob, 1);
    }

    /**
     * Gets the next QuizItem to send to the client
     *
     * @return The next quiz item
     * @throws IllegalStateException if this method is not called after {@code checkUserAnswer()}
     */
    public QuizItem getNextQuizItem() {
        if(currQuizItem != -1) {
            System.out.println("QuizProgress.java: getNextQuizItem was not called after checkUserAnswer with value: " + currQuizItem);
            throw new IllegalStateException();
        }

        int sumProb = 0;
        for(int i = 0; i < prob.length; i++) {
            sumProb += prob[i];
        }
        if(sumProb == 0) {// no more questions left
            for(int i = 0; i < prob.length; i++) {
                prob[i]++;
            }
            sumProb = prob.length;
        }
        int randInt = (int)(Math.random()*sumProb)+1;
        int idx = 0;
        int currSum = 1;
        while(!((currSum <= randInt) && (randInt < currSum+prob[idx]))) {
            currSum += prob[idx];
            idx++;
        }

        currQuizItem = idx;
        return server.getQuizManager().getQuizItemBy("ID", quizItemIDS.get(idx));
    }

    /**
     * Checks the users answer and updates the probabilities as neccessary
     *
     * @param response The user's response
     * @return {@code 1} If the response matches the correct answer (not case sensitive),
     * {@code 0} if it doesn't match, {@code -1} if this is a flashcard item
     * @throws IllegalStateException if this method is not called after {@code getNextQuizItem()}
     */
    public int checkUserAnswer(String response) {
        if(currQuizItem == -1) {
            throw new IllegalStateException();
        }

        QuizItem item = server.getQuizManager().getQuizItemBy("ID", quizItemIDS.get(currQuizItem));
        boolean correct = false;
        if(item instanceof Flashcard) {
            if(response.equals("correct")) {
                correct = true;
            }
        } else if(item instanceof MultipleChoice) {
            if(((MultipleChoice)item).getCorrectIndex() == Integer.parseInt(response)) {
                correct = true;
            }
        }

        if(correct) {
            prob[currQuizItem] = Math.max(prob[currQuizItem] - 2, 0);
        } else {
            prob[currQuizItem] = prob[currQuizItem] + 1;
        }
        currQuizItem = -1;

        if(item instanceof Flashcard) {
            return 2;
        } else if(correct) {
            return 1;
        } else {
            return 0;
        }
    }
}
