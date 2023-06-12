package web;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * An interface to form HTTP responses.
 *
 * @author Ricky Qin
 */
public class HTTPResponse {
    private int status;
    private HashMap<String, String> headerFields = new HashMap<String, String>();
    private byte[] body = new byte[0];

    /**
     * Gets the status message via code
     *
     * @param code The code of the status message
     * @return The status message in String format
     */
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
     *
     * @param type The extension (e.g. html, css). Doesn't contain the period before the extension.
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
     * Sets the status
     *
     * @param status The status
     * @return This HTTPResponse
     */
    public HTTPResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    /**
     * Adds a field to the header (excluding Content-Length)
     * @param name The name
     * @param value The field
     * @return This HTTPResponse
     */
    public HTTPResponse setHeaderField(String name, String value) {
        headerFields.put(name, value);
        return this;
    }

    /**
     * Appends to the body of the HTML
     *
     * @param str The string to append
     * @return This HTTPResponse
     */
    public HTTPResponse appendBody(String str) {
        try {
            return appendBody(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper function which appends to the body of the HTML
     *
     * @param bytes The bytes to append
     * @return This hTTP Response
     */
    public HTTPResponse appendBody(byte[] bytes) {
        byte[] newBody = new byte[body.length + bytes.length];
        for(int i = 0; i < body.length; i++) {
            newBody[i] = body[i];
        }
        for(int i = 0; i < bytes.length; i++) {
            newBody[i+body.length] = bytes[i];
        }
        body = newBody;
        return this;
    }

    /**
     * Writes and flushes this HTTP Response to the output stream
     *
     * @param output the output stream.
     */
    public void writeResponse(DataOutputStream output) {
        try {
            output.writeBytes("HTTP/1.1 " + status + " " + getStatusMessage(status) + "\n");
            headerFields.put("Content-Length", String.valueOf(body.length));
            for(Map.Entry<String, String> entry: headerFields.entrySet()) {
                output.writeBytes(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            output.write('\n');
            output.write(body);
            output.flush();
        } catch(IOException e) {
            System.out.println("Error occurred when writing to output stream");
            e.printStackTrace();
        }
    }
}
