package web.path;

import manager.UserManager;
import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

/**
 * A class that handles the changing password functionality
 */
public class ChangePasswordSubmit implements HTTPPath {

    /**
     * Logistics for changing a password. Checks if the passwords match, and updates accordingly
     *
     * @param request The request
     * @param server The server
     * @return The HTTP Response  containing the HTML and CSS
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        UserManager manager = server.getUserManager();

        // Check if user is logged in
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
        }

        String newPassword = request.getPostBody("newPassword");
        String oldPassword = request.getPostBody("oldPassword");

        if(manager.authenticateUser(credentials.first(), oldPassword) == null) {
            HTTPResponse response = new HTTPResponse().setStatus(200)
                    .setHeaderField("Content-Type", HTTPResponse.contentType("html"));
            response.appendBody("Invalid password!");
            return response;
        }

        boolean successfulChange = manager.changePassword(credentials.first(), newPassword, oldPassword);

        if(successfulChange) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/account-settings");
        } else {
            HTTPResponse response = new HTTPResponse().setStatus(200)
                    .setHeaderField("Content-Type", HTTPResponse.contentType("html"));
            response.appendBody("Failed to change password.");
            return response;
        }
    }
}
