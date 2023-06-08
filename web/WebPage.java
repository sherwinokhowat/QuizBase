package web;

import java.util.ArrayList;

/**
 * A class that constructs a webpage by appending webcomponents (either an object that implements
 * {@code WebComponent} or an HTML string representing a completed tag)
 * Contains common functionality for all webpages.
 *
 * @author Ricky Qin
 */
public class WebPage implements WebComponent {

    protected ArrayList<Object> headComponents = new ArrayList<Object>();
    protected ArrayList<Object> bodyComponents = new ArrayList<Object>();
    protected String bodyAttributes;

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
     * Sets the attributes of the <body> tag of this WebPage
     *
     * @param attributes attributes
     * @return This WebPage
     * @throws IllegalArgumentException if a component is neither a String or a {@code Webcomponent}
     */
    public WebPage setBodyAttributes(String attributes) {
        bodyAttributes = attributes;
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
        if(bodyAttributes == null) {
            str.append("<body>");
        } else {
            str.append("<body " + bodyAttributes + ">");
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
