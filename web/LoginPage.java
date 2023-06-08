package web;

public class LoginPage extends WebPage {
    @Override
    public String toHTMLString() {
        StringBuilder html = new StringBuilder();

        html.append("<html>");
        html.append("<body style='background-color: lightblue; display: flex; flex-direction: column; justify-content: center; align-items: center;'>");
        html.append("<img src='images/logo.png' style='width: 400px; height: auto;'>");
        html.append("<form style='display: flex; flex-direction: column; width: 200px; margin-top: 50px; padding: 20px;' action='/login/submit/' method='POST' style='background-color: lightgray;'>");
        html.append("<input style='margin-bottom: 3px;' type='text' id='username' name='username' placeholder='Username'>");
        html.append("<input style='margin-bottom: 5px;' type='password' id='password' name='password' placeholder='Password'>");
        html.append("<input type='submit' value='Login'>");
        html.append("</form>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
