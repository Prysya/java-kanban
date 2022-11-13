package main.exceptions;

import java.net.HttpURLConnection;

public class BadRequestException extends HttpException {
    public BadRequestException() {
        super(HttpURLConnection.HTTP_BAD_REQUEST);
    }
}
