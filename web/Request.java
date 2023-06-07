/**
 * Stores all the stuff related to the request (e.g. the file it's asking for, the host, etc. etc. etc.) Only accepts the raw request as the argument.
 * */

import java.util.ArrayList;

public class Request {
    private ArrayList<String> rawRequest; // stores everything, line by line.
    private String host;
    private String fileRequested;

    private String requestType;

    /**
     * Store useful information about the request
     * @param rawRequest An ArrayList containing all the lines in the request, line by line.
     * */
    public Request (ArrayList<String> rawRequest) {
        this.rawRequest = rawRequest;
        // System.out.println("DEBUG: request has " + rawRequest.size() + " lines in it.");
        String firstLine = rawRequest.get(0);
        String[] getRequestArray = firstLine.split(" ");
        this.requestType = getRequestArray[0];
        this.fileRequested = getRequestArray[1];
        String secondLine = rawRequest.get(1);
        String[] hostArray = secondLine.split(" ");
        // do some processing in here.
        this.host = hostArray[1];
    }

    public String getHost() {
        return host;
    }

    public String getFileName() {
        return fileRequested;
    }

    public String getRequestType() { return requestType; }

    /**
     * Returns the original request
     * @return the original request, separated line by line like the original.
     * */
    public String toString() {
        String totalString = "";
        for (int i = 0; i < rawRequest.size(); i++) {
            totalString += rawRequest.get(i);
        }
        return totalString;
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