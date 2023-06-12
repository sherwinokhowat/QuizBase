package web.path;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import web.HTTPRequest;
import web.HTTPResponse;
import web.Server;

/**
 * Takes files in the folder directory and uploads them.
 * e.g. if the file is QuizBase/images/x.png and the program sends a request for /images/x.png,
 * then this program handles the request.
 *
 * @author Avery Chan, Ricky Qin
 */

public class FilePath implements HTTPPath {

    public FilePath () {
    }

    @Override
    public HTTPResponse processRequest(HTTPRequest request, Server server) {
        HTTPResponse response = new HTTPResponse().setStatus(200);

        byte[] data = null; // byte array to store image
        File file = new File(System.getProperty("user.dir"), request.getPathWithoutQueryString());
        BufferedInputStream in = null;
        try {
            System.out.println("Opening file: " + file.getCanonicalPath());
            in = new BufferedInputStream(new FileInputStream(file));

            System.out.println("File Size: " + file.length());
            data = new byte[(int)file.length()];
            in.read(data, 0, (int)file.length());
            in.close();
            response.appendBody(data);
            return response;

        } catch (IOException e) {
            e.printStackTrace();
            return new HTTPResponse().setStatus(500);
        }
    }

}
