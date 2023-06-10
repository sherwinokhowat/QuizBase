package web.path;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import web.HTTP;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

public class ImagePath implements HTTPPath {

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTP.contentType("html"));

        byte[] data = null; // byte array to store image
        File imgFile = new File(System.getProperty("user.dir"), request.getPathWithoutQueryString());
        BufferedInputStream in = null;
        try {
            System.out.println("Opening file: " + imgFile.getCanonicalPath());
            in = new BufferedInputStream(new FileInputStream(imgFile));

            System.out.println("File Size: " +imgFile.length());
            data = new byte[(int)imgFile.length()];
            in.read(data, 0, (int)imgFile.length());
            in.close();
            response.appendBody(data);
            return response;

        } catch (IOException e) {
            e.printStackTrace();
            return new HTTPResponse().setStatus(500);
        }
    }

}
