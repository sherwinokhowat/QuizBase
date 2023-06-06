package struct;

/**
 * Interface that represents the methods that an object with web components
 * requires and can be rendered as HTML.
 * 
 * @author Sherwin Okhowat and
 */
public interface WebComponent {
    /**
     * Converts the component to an HTML string
     * 
     * @return the HTML string representation of the component
     */
    public String toHTMLString();

    /**
     * Returns the number of bytes
     * 
     * @return the number of bytes
     */
    public int getLength();
}
