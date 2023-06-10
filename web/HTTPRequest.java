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

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

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

    public String getPathWithoutQueryString() {
        int index = path.indexOf('?');
        if(index == -1) {
            return path;
        } else {
            return path.substring(0, index);
        }
    }

    public String getQueryString() {
        int index = this.path.indexOf('?');
        if (index == -1) {
            // No query string in this request
            return null;
        } else {
            return this.path.substring(index + 1);
        }
    }

}

/*
GET /style123.css HTTP/1.1
Host: 127.0.0.1:5000
Connection: keep-alive
sec-ch-ua: "Google Chrome";v="113", "Chromium";v="113", "Not-A.Brand";v="24"
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36
sec-ch-ua-platform: "Windows"
Accept: text/css,q=0.1
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: no-cors
Sec-Fetch-Dest: style
Referer: http://127.0.0.1:5000/
Accept-Encoding: gzip, deflate, br
Accept-Language: en-US,en;q=0.9
*/