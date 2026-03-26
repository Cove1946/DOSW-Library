package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    private final Map<Book, Integer> books = new HashMap<>();
    private final BookValidator bookValidator;

    public BookService(BookValidator bookValidator) {
        this.bookValidator = bookValidator;
    }

    public void addBook(Book book, int copies) {
        bookValidator.validate(book);
        bookValidator.validateCopies(copies);
        books.put(book, copies);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.keySet());
    }

    public Book getBookById(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del libro no puede estar vacío");
        return books.keySet().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public void deleteBook(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del libro no puede estar vacío");
        Book book = books.keySet()
                .stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        books.remove(book);
    }

    public void decreaseAvailableCopies(String id) {
        Book book = getBookById(id);
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No hay copias disponibles para el libro: " + id);
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
    }

    public void increaseAvailableCopies(String id) {
        Book book = getBookById(id);
        if (book.getAvailableCopies() >= book.getTotalCopies()) {
            throw new IllegalStateException(
                    "Las copias disponibles ya estan al máximo para el libro: " + id
            );
        }
        book.setAvailableCopies(book.getAvailableCopies() + 1);
    }

    public void updateTotalCopies(String id, int newTotal) {
        ValidationUtil.validateNotBlank(id, "El ID del libro no puede estar vacío");
        if (newTotal <= 0) {
            throw new IllegalArgumentException("El total de ejemplares debe ser mayor a 0");
        }
        Book book = getBookById(id);
        book.setTotalCopies(newTotal);
        if (book.getAvailableCopies() > newTotal) {
            book.setAvailableCopies(newTotal);
        }
    }
}