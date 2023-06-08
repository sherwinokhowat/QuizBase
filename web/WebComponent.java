package web;

/**
 * Interface that represents the methods that an object with web components
 * requires and can be rendered as HTML.
 *
 * @author Sherwin Okhowat
 */
public interface WebComponent {


    /**
     * Converts the component to an HTML string
     *
     * @return the HTML string representation of the component
     */
    public String toHTMLString();
}
