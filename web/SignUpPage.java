package web;

/**
 * Class representing the signup page of the server
 *
 * @author Sherwin Okhowat, Ricky Qin
 */
public class SignUpPage extends WebPage {

    /**
     * Constructs a SignUpPage
     */
    public SignUpPage() {
        appendBodyComponents("<img src='../images/logo.png' style='width: 400px; height: auto;'>",
        "<form style='display: flex; flex-direction: column; width: 200px; margin-top: 50px; padding: 20px;' action='/signup/submit' method='POST' style='background-color: lightgray;'>",
        "<input style='margin-bottom: 3px;' type='text' id='username' name='username' placeholder='Username'>",
        "<input style='margin-bottom: 5px;' type='password' id='password' name='password' placeholder='Password'>",
        "<input type='submit' value='Sign Up'>",
        "</form>");
        setBodyAttributes("style='background-color: lightblue; display: flex; flex-direction: column; justify-content: center; align-items: center;");
    }
}
