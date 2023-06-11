package web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Stores all the stuff related to the request (e.g. the file it's asking for, the host, etc. etc. etc.) Only accepts the raw request as the argument.
 *
 * @author Avery Chan, Ricky Qin, Sherwin Okhowat
 */
public class HTTPRequest {

    private String type; // e.g. POST, GET
    private String path; // e.g. /index2.html
    private String body;

    private ArrayList<String> rawRequest;// stores everything, line by line.
    private HashMap<String, String> fields = new HashMap<String, String>();
    private HashMap<String, String> postBody = new HashMap<>();// The contents of the body (only for POST)

    /**
     * Store useful information about the request
     *
     * @param rawRequest An ArrayList containing all the lines in the request, line by line.
     * */
    public HTTPRequest (ArrayList<String> rawRequest) {
        this.rawRequest = rawRequest;
        // System.out.println("DEBUG: request has " + rawRequest.size() + " lines in it.");

        String[] firstLine = rawRequest.get(0).split(" ");
        this.type = firstLine[0];
        this.path = firstLine[1];

        for(int i = 1; i < rawRequest.size(); i++) {
            String line = rawRequest.get(i);
            if(line.equals("")) {
                break;
            }
            int idx = line.indexOf(':');
            String fieldName = line.substring(0, idx).trim();
            String fieldValue = line.substring(idx+1).trim();
            fields.put(fieldName, fieldValue);
        }

        this.body = rawRequest.get(rawRequest.size()-1);
        if(this.type.equals("POST")) {
            StringTokenizer st = new StringTokenizer(body, "&");
            while(st.hasMoreTokens()) {
                String entry = st.nextToken();
                String key = entry.substring(0, entry.indexOf("="));
                String value = entry.substring(entry.indexOf("=")+1);
                postBody.put(key, value);
            }
        }
    }

    /**
     * Gets the type of request (eg POST, GET)
     *
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the full path of this request
     *
     * @return The path
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the value of a header field
     *
     * @param fieldName The field name
     * @return The field value
     */
    public String getField(String fieldName) {
        return fields.get(fieldName);
    }

    /**
     * Gets the decoded value of a key value pair in the request body.
     *
     * @param key The key
     * @return The value (already decoded)
     */
    public String getPostBody(String key) {
        String value = postBody.get(key);
        if(value != null) {
            return HTTP.decodeURL(value);
        }
        return null;
    }

    /**
     * Returns the original request
     * @return the original request, separated line by line like the original.
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < rawRequest.size(); i++) {
            str.append(rawRequest.get(i));
            if(i != rawRequest.size()-1) {
                str.append("\n");
            }
        }
        return str.toString();
    }

    /**
     * Gets the HTTP path, excluding the query string
     *
     * @return The HTTP path
     */
    public String getPathWithoutQueryString() {
        int index = path.indexOf('?');
        if(index == -1) {
            return path;
        } else {
            return path.substring(0, index);
        }
    }

    /**
     * Gets the query string in the HTTP path
     *
     * @return The query string (excluding {@code ?}) or {@code ""} if it doesn't exist.
     */
    public String getQueryString() {
        int index = this.path.indexOf('?');
        if (index == -1) {
            // No query string in this request
            return "";
        } else {
            return this.path.substring(index + 1);
        }
    }

}
