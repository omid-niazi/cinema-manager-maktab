package ir.bootcamp.cinema.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
