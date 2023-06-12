package web.path;

import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import web.WebPage;

/**
 * Class representing the signup page of the server
 *
 * @author Sherwin Okhowat, Ricky Qin
 */
public class LoginPage extends WebPage implements HTTPPath {

    /**
     * Constructs a LoginPage
     */
    public LoginPage() {
        appendBodyComponents("<div style='height: 165px;'></div>", "<img src='../images/logo.png' style='width: 400px; height: auto;'>",

                "<form style='display: flex; flex-direction: column; width: 200px; margin-top: 50px; padding: 20px;' action='/login/submit' method='POST' style='background-color: lightgray;'>",
                "<input style='margin-bottom: 3px;' type='text' id='username' name='username' placeholder='Username' minlength='3'>",
                "<input style='margin-bottom: 5px;' type='password' id='password' name='password' placeholder='Password' minlength='3'>",
                "<input type='submit' value='Login'>",
                "</form>",

                "<form style='display: flex; flex-direction: column; justify-content: center; align-items: center; width: 200px; margin-top: 10px; padding: 20px;' action='/signup' method='GET'>",
                "<p style='font-size: 12px'>Don't have an account? Make one!</p>",
                "<input type='submit' value='Sign Up'>",
                "</form>");

        setStyle("background-color: lightblue; display: flex; flex-direction: column; justify-content: center; align-items: center; font-family: Arial");
    }


    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials != null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/home");
        }
        return new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"))
                .appendBody(toHTMLString());
    }
}