package web;

public class SignUpPage extends WebPage {
    @Override
    public String toHTMLString() {
        StringBuilder html = new StringBuilder();
        html.append("<form action='/submit-signup' method='post'>");
        html.append("<label for='username'>Username</label>");
        html.append("<br>");
        html.append("<input type='text' id='username' name='username'");
        html.append("<br>");
        html.append("<br>");
        html.append("<label for='password'>Password</label>");
        html.append("<br>");
        html.append("<input type='password' id='password' name='password'");
        html.append("<br>");
        html.append("<br>");
        html.append("<input type='submit' value='Sign Up'>");
        html.append("</form>");
        return html.toString();
    }
}
