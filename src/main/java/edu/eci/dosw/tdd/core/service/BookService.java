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

    public void updateBookAvailability(String id, boolean available) {
        ValidationUtil.validateNotBlank(id, "El ID del libro no puede estar vacío");
        Book book = getBookById(id);
        book.setAvailable(available);
    }

    public int getCopies(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del libro no puede estar vacío");
        Book book = getBookById(id);
        return books.get(book);
    }

    public void updateCopies(String id, int copies) {
        ValidationUtil.validateNotBlank(id, "El ID del libro no puede estar vacío");
        Book book = getBookById(id);
        books.put(book, copies);
    }

    public void deleteBook(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del libro no puede estar vacío");
        Book book = getBookById(id);
        books.remove(book);
    }
}