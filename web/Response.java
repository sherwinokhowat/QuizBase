package web;

import java.util.HashMap;
import java.util.Map;

/**
 * An interface to form HTTP responses.
 *
 * @author Ricky Qin
 */
public class Response {

    public static String getStatusMessage(int s) {
        switch(s) {
            case 200:
                return "OK";
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

    private int status;
    private HashMap<String, String> headerFields = new HashMap<String, String>();
    private String body = "";

    public Response(int status) {
        this.status = status;
    }

    /**
     * Adds a field to the header (excluding Content-Length)
     * @param name The name
     * @param value The field
     * @return
     */
    public Response addHeaderField(String name, String value) {
        headerFields.put(name, value);
        return this;
    }

    public Response appendBody(String str) {
        body += str;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("HTTP/1.1 " + status + " " + getStatusMessage(status) + "\n");
        headerFields.put("Content-Length", String.valueOf(body.length()));
        for(Map.Entry<String, String> entry: headerFields.entrySet()) {
            str.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
        str.append("\n");
        str.append(body);
        return str.toString();
    }
}
