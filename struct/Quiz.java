package struct;

import java.util.PriorityQueue;
import manager.QuizManager;

public class Quiz {
    private PriorityQueue<QuizItem> quizItems;
    private int likes;
    private String name;
    private String description;
    private int id;
    private User creator;
    private HashMap<User, Integer> leaderboard;
    private QuizManager quizManager;

    public Quiz(QuizManager q) {
        this.quizManager = q;
        // unimplemented
    }

    public int getLikes() {
        return this.likes;
    }

    public void addLike(User user) {
        this.likes++;
        // unimplemented
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public User getCreator() {
        return this.creator;
    }

    /**
     * +addItem(QuizItem q) : void - requires database connection
+ removeItem(QuizItem q) : void - requires database connection
+ setName (String newName) boolean - database connection
+ setDescription (String newDescription) boolean - database connection
     */

     public void addItem(QuizItem item) {
         this.quizItems.add(item);
        // unimplemented
     }

     public void removeItem(QuizItem item) {
         this.quizItems.remove(item);
     }     
    
}
