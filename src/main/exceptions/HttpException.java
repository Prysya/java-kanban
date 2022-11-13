package main.exceptions;

import java.io.IOException;

public class HttpException extends IOException {
    /** Код ошибки */
    private final int errorCode;

    public HttpException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
