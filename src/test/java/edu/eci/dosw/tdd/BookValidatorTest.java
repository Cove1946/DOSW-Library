package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookValidatorTest {

    private BookValidator bookValidator;

    @BeforeEach
    void setUp() {
        bookValidator = new BookValidator();
    }

    @Test
    void shouldPassValidationForValidBook() {
        Book book = new Book("B001", "Clean Code", "Martin", true);
        assertDoesNotThrow(() -> bookValidator.validateBook(book));
    }

    @Test
    void shouldPassValidationForPositiveCopies() {
        assertDoesNotThrow(() -> bookValidator.validateCopies(5));
    }

    @Test
    void shouldPassValidationForZeroCopies() {
        assertDoesNotThrow(() -> bookValidator.validateCopies(0));
    }


    @Test
    void shouldThrowWhenBookIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> bookValidator.validateBook(null));
    }

    @Test
    void shouldThrowWhenBookIdIsBlank() {
        Book book = new Book("", "Clean Code", "Martin", true);
        assertThrows(IllegalArgumentException.class,
                () -> bookValidator.validateBook(book));
    }

    @Test
    void shouldThrowWhenBookTitleIsBlank() {
        Book book = new Book("B001", "", "Martin", true);
        assertThrows(IllegalArgumentException.class,
                () -> bookValidator.validateBook(book));
    }

    @Test
    void shouldThrowWhenBookAuthorIsBlank() {
        Book book = new Book("B001", "Clean Code", "", true);
        assertThrows(IllegalArgumentException.class,
                () -> bookValidator.validateBook(book));
    }

    @Test
    void shouldThrowWhenCopiesAreNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> bookValidator.validateCopies(-1));
    }
}