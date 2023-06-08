package web;

/**
 * Class representing the signup page of the server
 *
 * @author Sherwin Okhowat
 */
public class SignUpPage extends WebPage {
    @Override
    public String toHTMLString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>");
        sb.append("<body style='background-color: lightblue; display: flex; flex-direction: column; justify-content: center; align-items: center;'>");
        sb.append("<img src='images/logo.png' style='width: 400px; height: auto;'>");
        sb.append("<form style='display: flex; flex-direction: column; width: 200px; margin-top: 50px; padding: 20px;'>");
        sb.append("<input style='margin-bottom: 3px;' type='text' id='username' name='username' placeholder='Username'>");
        sb.append("<input style='margin-bottom: 5px;' type='password' id='password' name='password' placeholder='Password'>");
        sb.append("<input type='submit' value='Sign Up'>");
        sb.append("</form>");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }
}
