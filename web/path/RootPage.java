package web.path;

import web.HTTP;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Hyperlink;
import web.Server;
import web.WebPage;

public class RootPage implements HTTPPath {

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        WebPage webPage = new WebPage().appendBodyComponents(
                new Hyperlink("/login", "Log in", true),
                WebPage.BR_TAG,
                new Hyperlink("/signup", "Sign up", true));

        return new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTP.contentType("html"))
                .appendBody(webPage.toHTMLString());
    }

}
