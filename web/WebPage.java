package web;

/**
 * 
 * 
 */
public abstract class WebPage implements WebComponent {

    // common functionality for all webpages
    protected String getBackgroundHTML() {
        return "<body style='background-color: lightblue; display: flex; flex-direction: column; justify-content: center; align-items: center;'>";
    }
}
