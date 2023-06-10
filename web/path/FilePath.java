package web.path;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import web.HTTP;
import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

/**
 * Takes files in the folder directory and uploads them. e.g. if the file is QuizBase/images/x.png and the program sends a request for /images/x.png, then this program handles the request.
 */

public class FilePath implements HTTPPath {

    private String fileType;

    public FilePath (String fileType) {
        this.fileType = fileType;
    }

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        HTTPResponse response = new HTTPResponse().setStatus(200)
                .setHeaderField("Content-Type", HTTP.contentType(fileType));

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
