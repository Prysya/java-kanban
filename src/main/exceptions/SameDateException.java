package main.exceptions;

public class SameDateException extends IllegalArgumentException {
    public SameDateException(String message) {
        super(message);
    }
}