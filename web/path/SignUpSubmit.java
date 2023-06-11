package web.path;

import struct.User;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Hyperlink;
import web.Server;
import web.WebPage;

/**
 * Class which handles a signup related request
 *
 * @author Sherwin Okhowat, Ricky Qin
 */
public class SignUpSubmit implements HTTPPath {

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        String username = request.getPostBody("username");
        String password = request.getPostBody("password");

        if ("/login".equals(request.getPathWithoutQueryString())) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
        }

        User user = server.getUserManager().registerUser(username, password);

        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"));
        if(user == null) {
            response.appendBody("Unable to sign up! This may be because your username has already been taken, credentials are invalid or a network error occurred.");
        } else {
            WebPage webPage = new WebPage().appendBodyComponents("Sign up successful!", WebPage.BR_TAG,
                    new Hyperlink("../../login", "Log in", true));
            response.appendBody(webPage.toHTMLString());
        }
        return response;
    }
}
