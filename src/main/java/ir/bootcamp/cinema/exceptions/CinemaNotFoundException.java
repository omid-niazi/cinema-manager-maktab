package ir.bootcamp.cinema.exceptions;

public class CinemaNotFoundException extends RuntimeException {
    public CinemaNotFoundException() {
    }

    public CinemaNotFoundException(String message) {
        super(message);
    }
}
