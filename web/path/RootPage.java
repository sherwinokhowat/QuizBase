package web.path;

import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

public class RootPage implements HTTPPath {

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
    }
}
