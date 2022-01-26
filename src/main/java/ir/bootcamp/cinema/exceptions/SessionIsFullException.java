package ir.bootcamp.cinema.exceptions;

public class SessionIsFullException extends RuntimeException {
    public SessionIsFullException() {
    }

    public SessionIsFullException(String message) {
        super(message);
    }
}
