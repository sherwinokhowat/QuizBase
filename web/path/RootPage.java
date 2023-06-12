package web.path;

import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

/**
 * Class representing the root page
 *
 * @author Ricky Qin
 */
public class RootPage implements HTTPPath {

    /**
     * Redirects the user to the rootpage which is the login page
     *
     * @param request The request
     * @param server The server
     * @return A redirection to the login page
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
    }
}
