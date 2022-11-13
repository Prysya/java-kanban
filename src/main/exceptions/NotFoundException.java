package main.exceptions;

import java.net.HttpURLConnection;

public class NotFoundException extends HttpException {
    public NotFoundException() {
        super(HttpURLConnection.HTTP_BAD_METHOD);
    }
}
