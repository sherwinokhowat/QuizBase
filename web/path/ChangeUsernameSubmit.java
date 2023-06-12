package web.path;

import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import utility.Pair;
import manager.UserManager;

/**
 * Class which handles a request to change the username
 *
 * @author Sherwin Okhowat
 */
public class ChangeUsernameSubmit implements HTTPPath {

    /**
     * Logistics for changing a username. Checks if the passwords match, and updates accordingly.
     *
     * @param request The request
     * @param server The server
     * @return The HTTP Response containing the HTML and CSS
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        UserManager manager = server.getUserManager();

        // Check if user is logged in
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
        }

        String newUsername = request.getPostBody("newUsername");
        String password = request.getPostBody("password");

        if(manager.authenticateUser(credentials.first(), password) == null) {
            HTTPResponse response = new HTTPResponse().setStatus(200)
                    .setHeaderField("Content-Type", HTTPResponse.contentType("html"));
            response.appendBody("Invalid password!");
            return response;
        }

        boolean successfulChange = manager.changeUsername(credentials.first(), newUsername, password);

        if(successfulChange) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/account-settings");
        } else {
            HTTPResponse response = new HTTPResponse().setStatus(200)
                    .setHeaderField("Content-Type", HTTPResponse.contentType("html"));
            response.appendBody("Failed to change username. The new username may already be taken.");
            return response;
        }
    }
}

