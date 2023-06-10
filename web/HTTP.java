package web;

/**
 * Contains useful constants and methods relating to HTTP
 *
 * @author Ricky Qin
 */
public class HTTP {

    public static String getStatusMessage(int s) {
        switch(s) {
            case 200:
                return "OK";
            case 303:
                return "See Other";
            case 400:
                return "Bad Request";
            case 401:
                return "Unauthorized";
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            default:
                return "";
        }
    }

    /**
     * Returns the MIME type of a given file extension.
     * @param extension The extension (e.g. html, css). Doesn't contain the period before the extension.
     * @return a string representing the MIME type of the extension, or "invalid" if it can't find the type.
     * */
    public static String contentType(String extension) {
        String result;
        switch (extension) {
            case "html":
                result = "text/html";
                break;
            case "css":
                result = "text/css";// could look into CSS support for.
                break;
            case "js":
                result = "application/javascript";
                break;
            case "png":
                result = "image/png";
                break;
            case "jpg":
            case "jpeg":
                result = "image/jpeg";
                break;
            case "gif":
                result = "image/gif";
                break;
            default:
                result = "invalid";
                break;
        }
        return result;
    }

    /**
     * Cannot be instantiated
     */
    private HTTP(){}
}
