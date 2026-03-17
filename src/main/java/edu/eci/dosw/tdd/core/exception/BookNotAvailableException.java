package edu.eci.dosw.tdd.core.exception;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String bookId) {
        super("El libro con ID " + bookId + " no está disponible.");
    }
}
