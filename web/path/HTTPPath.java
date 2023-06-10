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
    public HTTPResponse processRequest(HTTPRequest request, Server server);
}
