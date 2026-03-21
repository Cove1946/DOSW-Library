package edu.eci.dosw.tdd;


import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(new BookValidator());
    }

    // ─── EXITOSOS ─────────────────────────────────────────

    @Test
    void addBook_shouldAddBookSuccessfully() {
        Book book = new Book("B1", "Clean Code", "Robert Martin", true);
        bookService.addBook(book, 3);
        assertEquals(book, bookService.getBookById("B1"));
    }

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        bookService.addBook(new Book("B1", "Clean Code", "Robert Martin", true), 3);
        bookService.addBook(new Book("B2", "El Quijote", "Cervantes", true), 2);
        List<Book> books = bookService.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    void updateBookAvailability_shouldUpdateCorrectly() {
        bookService.addBook(new Book("B1", "Clean Code", "Robert Martin", true), 3);
        bookService.updateBookAvailability("B1", false);
        assertFalse(bookService.getBookById("B1").isAvailable());
    }

    @Test
    void deleteBook_shouldRemoveBookFromList() {
        bookService.addBook(new Book("B1", "Clean Code", "Robert Martin", true), 3);
        bookService.deleteBook("B1");
        assertEquals(0, bookService.getAllBooks().size());
    }

    // ─── ERROR ────────────────────────────────────────────

    @Test
    void addBook_shouldThrowException_whenBookIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                bookService.addBook(null, 3)
        );
    }

    @Test
    void addBook_shouldThrowException_whenTitleIsBlank() {
        Book book = new Book("B1", "", "Robert Martin", true);
        assertThrows(IllegalArgumentException.class, () ->
                bookService.addBook(book, 3)
        );
    }

    @Test
    void addBook_shouldThrowException_whenCopiesIsZero() {
        Book book = new Book("B1", "Clean Code", "Robert Martin", true);
        assertThrows(IllegalArgumentException.class, () ->
                bookService.addBook(book, 0)
        );
    }

    @Test
    void getBookById_shouldThrowException_whenBookNotFound() {
        assertThrows(RuntimeException.class, () ->
                bookService.getBookById("B99")
        );
    }
}