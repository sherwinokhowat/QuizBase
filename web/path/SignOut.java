package web.path;

import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

/**
 * A sign out HTTP path (signs out the user)
 *
 * @author Ricky Qin
 */
public class SignOut implements HTTPPath {

    /**
     * Processes the request for sign-out functionality.
     *
     * @param request The request
     * @param server The server
     * @return A sign-out HTTPResponse path
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/");
        } else {
            server.deleteSessionID(request);
            return new HTTPResponse().setStatus(200)
                    .setHeaderField("Content-Type", HTTPResponse.contentType("html"))
                    .setHeaderField("Set-Cookie", "sessionId=none; Path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT")
                    .appendBody("<html><head><meta http-equiv='refresh' content='2'></head><body><p>Signed out! Redirecting you back... </p></body></html>");
        }
    }

}