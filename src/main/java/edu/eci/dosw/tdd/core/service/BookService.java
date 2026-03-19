package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

    private final Map<Book, Integer> bookInventory = new HashMap<>();
    private final BookValidator bookValidator;

    public BookService(BookValidator bookValidator) {
        this.bookValidator = bookValidator;
    }

    public void addBook(Book book, int copies) {
        bookValidator.validateBook(book);
        bookValidator.validateCopies(copies);
        bookInventory.merge(book, copies, Integer::sum);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(bookInventory.keySet());
    }

    public Book getBookById(String id) {
        return bookInventory.keySet().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public void updateAvailability(String id, boolean available) {
        Book book = getBookById(id);
        book.setAvailable(available);
    }

    public int getCopies(String bookId) {
        Book book = getBookById(bookId);
        return bookInventory.getOrDefault(book, 0);
    }

    public void decrementCopy(String bookId) {
        Book book = getBookById(bookId);
        int copies = bookInventory.get(book);
        if (copies <= 1) {
            bookInventory.put(book, 0);
            book.setAvailable(false);
        } else {
            bookInventory.put(book, copies - 1);
        }
    }

    public void incrementCopy(String bookId) {
        Book book = getBookById(bookId);
        bookInventory.merge(book, 1, Integer::sum);
        book.setAvailable(true);
    }

    public Map<Book, Integer> getInventory() {
        return Collections.unmodifiableMap(bookInventory);
    }
}