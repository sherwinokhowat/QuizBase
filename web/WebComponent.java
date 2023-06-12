package web;

/**
 * Interface that represents the methods that an object with web components
 * requires and can be rendered as HTML.
 *
 * @author Sherwin Okhowat, Ricky Qin
 */
public interface WebComponent {

    /**
     * Sets the style attribute of this component
     *
     * @param style The style
     * @return This WebComponent
     */
    public WebComponent setStyle(String style);

    /**
     * Converts the component to an HTML string
     *
     * @return the HTML string representation of the component
     */
    public String toHTMLString();
}
