package web;

/**
 * A class that represents an HTML hyperlink.
 *
 * @author Ricky Qin
 */
public class Hyperlink implements WebComponent {

    private String href;

    private String linkText;

    private boolean isButton;

    private String style;

    /**
     * Constructs a Hyperlink object
     *
     * @param href The URL to link to
     * @param linkText The text to display for this link
     * @param isButton Whether the button should be formatted as a button
     */
    public Hyperlink(String href, String linkText, boolean isButton) {
        this.href = href;
        this.linkText = linkText;
        this.isButton = isButton;
    }

    @Override
    public WebComponent setStyle(String style) {
        this.style = style;
        return this;
    }

    @Override
    public String toHTMLString() {
        StringBuilder str = new StringBuilder();
        str.append("<a href=\""+href+"\"");
        if(style != null) {
            str.append("style=\""+style+"\"");
        }
        str.append(">");
        if(isButton) {
            str.append("<button>");
        }
        str.append(linkText);
        if(isButton) {
            str.append("</button>");
        }
        str.append("</a>");
        return str.toString();
    }
}
