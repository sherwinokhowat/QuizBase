package web.path;
import utility.Pair;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;
import web.WebPage;
import manager.UserManager;

/**
 * A class representing the account settings page
 *
 * @author Sherwin Okhowat
 */
public class AccountSettingsPage extends WebPage implements HTTPPath {
    /**
     * Constructs an AccountSettingsPage
     */
    public AccountSettingsPage() {
        setStyle("background-color: lightblue; overflow-x: hidden; display: flex; flex-direction: column; align-items: center; box-sizing: border-box;");
    }

    /**
     * Whenever a request is generated for this page, this method figures out what to do with it.
     * @param request The request
     * @param server The server
     * @return The HTTP Response containing the HTML and CSS
     */
    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);
        UserManager manager = server.getUserManager();
        if(credentials == null) {
            return new HTTPResponse().setStatus(303).setHeaderField("Location", "/login");
        }

        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTPResponse.contentType("html"));

        addHeader(request, server);

        appendBodyComponents(
                "<h3 style='margin: 0; padding-top: 0;'>Change Username</h3>",
                "<form style='display: flex; flex-direction: column; width: 200px; padding: 3px;' action='/account-settings/change-username-submit' method='POST'>",
                "<input type='text' id='newUsername' name='newUsername' minlength='3'; placeholder='New Username'>",
                "<input type='password' id='password' name='password' placeholder='Password'>",
                "<input type='submit' value='Change Username'>",
                "</form>",

                "<h3 style='margin: 0; padding-top: 0;'>Change Password</h3>",
                "<form style='display: flex; flex-direction: column; width: 200px; padding: 3px;' action='/account-settings/change-password-submit' method='POST'>",
                "<input type='password' id='oldPassword' name='oldPassword' placeholder='Old Password'>",
                "<input type='password' id='newPassword' name='newPassword' minlength='3'; placeholder='New Password'>",
                "<input type='submit' value='Change Password'>",
                "</form>");

        response.appendBody(toHTMLString());
        return response;
    }

}
