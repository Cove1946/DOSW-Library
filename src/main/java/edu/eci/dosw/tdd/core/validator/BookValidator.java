package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookValidator {

    public void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo.");
        }
        if (book.getId() == null || book.getId().isBlank()) {
            throw new IllegalArgumentException("El ID del libro no puede estar vacío.");
        }
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("El título del libro no puede estar vacío.");
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("El autor del libro no puede estar vacío.");
        }
    }

    public void validateCopies(int copies) {
        if (copies < 0) {
            throw new IllegalArgumentException("La cantidad de ejemplares no puede ser negativa.");
        }
    }
}