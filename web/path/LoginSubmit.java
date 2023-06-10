package web.path;

import struct.User;
import web.HTTP;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

public class LoginSubmit implements HTTPPath {

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        String username = request.getPostBody("username");
        String password = request.getPostBody("password");
        User user = server.getUserManager().authenticateUser(username, password);

        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTP.contentType("html"));
        if(user == null) {
            response.appendBody("Invalid credentials!");
        } else {
            response.setStatus(303).setHeaderField("Location", "/home");
            response.setHeaderField("Set-Cookie",
                    "sessionId="+server.createSessionID(username, password)+"; Path=/");
        }
        return response;
    }

}
