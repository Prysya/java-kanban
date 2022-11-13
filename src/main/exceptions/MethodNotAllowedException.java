package main.exceptions;

import java.net.HttpURLConnection;

public class MethodNotAllowedException extends HttpException {
    public MethodNotAllowedException() {
        super(HttpURLConnection.HTTP_BAD_METHOD);
    }
}
