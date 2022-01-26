package ir.bootcamp.cinema.exceptions;

public class SessionFinishedException extends RuntimeException {
    public SessionFinishedException() {
    }

    public SessionFinishedException(String message) {
        super(message);
    }
}
