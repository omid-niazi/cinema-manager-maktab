package ir.bootcamp.cinema.exceptions;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException() {
    }

    public SessionNotFoundException(String message) {
        super(message);
    }
}
