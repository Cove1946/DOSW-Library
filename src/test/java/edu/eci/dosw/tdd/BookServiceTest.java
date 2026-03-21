package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(new BookValidator());
    }

    @Test
    void shouldAddBookSuccessfully() {
        Book book = new Book("B001", "Clean Code", "Robert C. Martin", true);
        bookService.addBook(book, 3);
        assertEquals(3, bookService.getCopies("B001"));
    }

    @Test
    void shouldGetAllBooks() {
        bookService.addBook(new Book("B001", "Clean Code", "Martin", true), 2);
        bookService.addBook(new Book("B002", "Refactoring", "Fowler", true), 1);
        assertEquals(2, bookService.getAllBooks().size());
    }

    @Test
    void shouldGetBookByIdSuccessfully() {
        Book book = new Book("B001", "Clean Code", "Martin", true);
        bookService.addBook(book, 1);
        Book found = bookService.getBookById("B001");
        assertEquals("Clean Code", found.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById("NONEXISTENT"));
    }

    @Test
    void shouldUpdateAvailabilityToFalse() {
        bookService.addBook(new Book("B001", "Clean Code", "Martin", true), 2);
        bookService.updateAvailability("B001", false);
        assertFalse(bookService.getBookById("B001").isAvailable());
    }

    @Test
    void shouldDecrementCopyAndMarkUnavailableWhenLastCopy() {
        bookService.addBook(new Book("B001", "Clean Code", "Martin", true), 1);
        bookService.decrementCopy("B001");
        assertEquals(0, bookService.getCopies("B001"));
        assertFalse(bookService.getBookById("B001").isAvailable());
    }

    @Test
    void shouldIncrementCopyAndMarkAvailable() {
        Book book = new Book("B001", "Clean Code", "Martin", false);
        bookService.addBook(book, 0);
        bookService.incrementCopy("B001");
        assertEquals(1, bookService.getCopies("B001"));
        assertTrue(bookService.getBookById("B001").isAvailable());
    }
}
