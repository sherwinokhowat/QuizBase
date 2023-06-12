package web.path;

import struct.User;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

/**
 * Class which handles a login related request
 *
 * @author Sherwin Okhowat and Ricky Qin
 */
public class LoginSubmit implements HTTPPath {

    /**
     * Processes the submit logistics of the login page. Essentially checks whether the username and password exist in the database
     *
     * @param request The request
     * @param server The server
     * @return The appropriate HTTPResponse
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        String username = request.getPostBody("username");
        String password = request.getPostBody("password");
        User user = server.getUserManager().authenticateUser(username, password);

        if ("/signup".equals(request.getPathWithoutQueryString())) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/signup");
        }

        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"));
        if(user == null) {
            response.appendBody("<html><head><meta http-equiv='refresh' content='3;url=/login' /></head><body><p>Invalid Credentials! Redirecting you back... </p></body></html>");
        } else {
            response.setStatus(303).setHeaderField("Location", "/home");
            response.setHeaderField("Set-Cookie",
                    "sessionId="+server.createSessionID(username, password)+"; Path=/");
        }
        return response;
    }

}
