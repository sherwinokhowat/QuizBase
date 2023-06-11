package web;

import java.util.ArrayList;

import utility.Pair;

/**
 * A class that constructs a webpage by appending webcomponents (either an object that implements
 * {@code WebComponent} or an HTML string representing a completed tag)
 * Contains common functionality for all webpages.
 *
 * @author Ricky Qin
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
            return;
        } else {
            String signOutButtonStyle = "background-color: #F2F2F2; color: black; padding: 10px; text-decoration: none; border: 1px solid black;";

            // header
            appendBodyComponents("<div style='display: flex; justify-content: space-between; width: 100%; padding: 20px;'>",
                    "<a href='/home'><img src='../images/logo.png' style='width: 150px; height: auto;'></a>",
                    new Hyperlink("/create-quiz", "Create A Quiz", false), // ideally I would put this in a div so that an image could be next to the hyperlink, or just have this as an image
                    "<div style='text-align: right; font-size: 1.5em; padding-top: 35px; padding-right: 35px;'>" + credentials.first() + "</div>",
                    new Hyperlink("/signout", "Sign Out", false).setStyle(signOutButtonStyle),
                    "</div>");
            // line
            appendBodyComponents("<hr style='border: 2px solid black; width: 100%;'>");
        }
    }

    /**
     * Sets the "style" attribute of the body of this WebPage
     *
     * @param style The style
     * @return This WebPage
     */
    @Override
    public WebComponent setStyle(String style) {
        this.style = style;
        return this;
    }

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
