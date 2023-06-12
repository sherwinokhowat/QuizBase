package web.path;

import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

/**
 * An interface that describes the functionalities of an HTTP path
 *
 * @author Ricky Qin
 */
public interface HTTPPath {
    /**
     * Whenever a request is generated for this page, this method figures out what to do with it.
     * @param request The request
     * @param server The server
     * @return The HTTP Response
     */
    public HTTPResponse processRequest(HTTPRequest request, Server server);
}
