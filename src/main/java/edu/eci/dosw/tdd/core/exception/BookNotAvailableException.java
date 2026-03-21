package edu.eci.dosw.tdd.core.exception;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
