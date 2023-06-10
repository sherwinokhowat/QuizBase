package web;

/**
 * Contains useful constants and methods relating to HTTP
 *
 * @author Ricky Qin
 */
public class HTTP {

    public static String getStatusMessage(int code) {
        switch(code) {
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
            case 500:
                return "Internal Server Error";
            case 501:
                return "Not Implemented";
            default:
                throw new IllegalArgumentException("Status code "+code+" not supported");
        }
    }

    /**
     * Returns the MIME type of a given file extension.
     * @param extension The extension (e.g. html, css). Doesn't contain the period before the extension.
     * @return a string representing the MIME type of the extension, or "invalid" if it can't find the type.
     * */
    public static String contentType(String type) {
        switch (type) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";// could look into CSS support for.
            case "js":
                return "application/javascript";
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            default:
                throw new IllegalArgumentException("Content type \""+type+"\" not supported");
        }
    }

    /**
     * Cannot be instantiated
     */
    private HTTP(){}
}
