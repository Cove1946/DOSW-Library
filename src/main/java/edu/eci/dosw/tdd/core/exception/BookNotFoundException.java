package edu.eci.dosw.tdd.core.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String bookId) {
        super("Libro con ID " + bookId + " no encontrado.");
    }
}

