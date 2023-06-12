package web;

import java.util.ArrayList;

import utility.Pair;

/**
 * A class that constructs a webpage by appending webcomponents (either an object that implements
 * {@code WebComponent} or an HTML string representing a completed tag)
 * Contains common functionality for all webpages.
 *
 * @author Ricky Qin, Avery Chan and Sherwin Okhowat
 */
public class WebPage implements WebComponent {

    public static String BR_TAG = "<br>";

    protected ArrayList<Object> headComponents = new ArrayList<Object>();
    protected ArrayList<Object> bodyComponents = new ArrayList<Object>();
    protected String style;

    public WebPage() {
    }

    /**
     * Adds webcomponents to the html header
     *
     * @param components The webcomponents to add (varargs)
     * @return This WebPage
     * @throws IllegalArgumentException if a component is neither a String or a {@code Webcomponent}
     */
    public WebPage appendHeadComponents(Object... components) {
        for(Object component: components) {
            if((component instanceof WebComponent) || (component instanceof String)) {
                headComponents.add(component);
            } else {
                throw new IllegalArgumentException("Component must be a WebComponent or String object");
            }
        }
        return this;
    }

    /**
     * Adds webcomponents to the html body
     *
     * @param components The webcomponents to add (varargs)
     * @return This WebPage
     * @throws IllegalArgumentException if a component is neither a String or a {@code Webcomponent}
     */
    public WebPage appendBodyComponents(Object... components) {
        for(Object component: components) {
            if((component instanceof WebComponent) || (component instanceof String)) {
                bodyComponents.add(component);
            } else {
                throw new IllegalArgumentException("Component must be a WebComponent or String object");
            }
        }
        return this;
    }

    /**
     * Adds the header to the web page.
     * @param request 
     * @param server
     */
    public void addHeader(HTTPRequest request, Server server) {
        Pair<String, String> credentials = server.checkSessionID(request);

        if (credentials == null) {
            System.out.println("The header shouldn't be added before the user has been logged in.");
        } else {
            String buttonStyle = "background-color: #F2F2F2; color: black; margin-top: 4px; padding: 10px; text-decoration: none; border: 1px solid black; align-self: start; width: 120px;";

            appendBodyComponents("<div style='display: flex; justify-content: space-between; width: 100%; padding: 20px;'>",
                    "<div style='display: flex; flex-direction: column; align-items: start;'>",
                    "<a href='/home'><img src='/images/logo.png' style='width: 340px; height: auto;'></a>",
                    "<div style='text-align: left; font-size: 20px; padding-top: 5px;'>" + credentials.first() + "</div>",
                    "</div>",
                    "<div class='button-container' style='display: flex; flex-direction: column;'>",
                    new Hyperlink("/signout", "Sign Out", false).setStyle(buttonStyle),
                    new Hyperlink("/account-settings", "Account Settings", false).setStyle(buttonStyle),
                    "</div></div>");

            appendBodyComponents("<hr style='border: 2px solid black; width: 100%;'>");
        }
    }



    /**
     * Sets the "style" attribute of the body of this WebPage. Used for general style rules, not for one specific element.
     *
     * @param style The style
     * @return This WebPage
     */
    @Override
    public WebComponent setStyle(String style) {
        this.style = style;
        return this;
    }

    /**
     * Returns the HTML code that makes up the website.
     * @return the HTML code that makes up the website.
     */
    @Override
    public String toHTMLString() {
        StringBuilder str = new StringBuilder();
        str.append("<html>");
        str.append("<head>");
        for(Object component: headComponents) {
            if(component instanceof String) {
                str.append((String)component);
            } else {
                str.append(((WebComponent)component).toHTMLString());
            }
        }
        str.append("</head>");
        if(style == null) {
            str.append("<body>");
        } else {
            str.append("<body style='" + style + "'>");
        }
        for(Object component: bodyComponents) {
            if(component instanceof String) {
                str.append((String)component);
            } else {
                str.append(((WebComponent)component).toHTMLString());
            }
        }
        str.append("</body>");
        str.append("</html>");
        return str.toString();
    }
}
