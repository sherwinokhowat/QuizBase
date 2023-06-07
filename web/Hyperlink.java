package web;

/**
 * A class that represents an HTML hyperlink.
 *
 * @author Ricky Qin
 */
public class Hyperlink implements WebComponent {

    private String href;

    private String linkText;

    /**
     * Constructs a Hyperlink object
     *
     * @param href The URL to link to
     * @param linkText The text to display for this link
     */
    public Hyperlink(String href, String linkText) {
        this.href = href;
        this.linkText = linkText;
    }

    @Override
    public String toHTMLString() {
        return "<a href=\""+href+"\">"+linkText+"</a>";
    }
}
