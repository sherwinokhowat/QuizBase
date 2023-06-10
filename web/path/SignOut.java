package web.path;

import utility.Pair;
import web.HTTP;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

/**
 * A sign out HTTP path (signs out the user)
 *
 * @author Ricky Qin
 */
public class SignOut implements HTTPPath {

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/");
        } else {
            server.deleteSessionID(request);
            return new HTTPResponse().setStatus(200)
                    .setHeaderField("Content-Type", HTTP.contentType("html"))
                    // even if the client doesn't delete the cookie, it won't work anymore
                    .setHeaderField("Set-Cookie", "sessionId=none; Path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT")
                    .appendBody("Signed out!");
        }
    }

}
