package ir.bootcamp.cinema.exceptions;

public class CinemaIsOccupyException extends RuntimeException {
    public CinemaIsOccupyException() {
    }

    public CinemaIsOccupyException(String message) {
        super(message);
    }
}
