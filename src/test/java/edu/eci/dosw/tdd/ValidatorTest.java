package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.validator.BookValidator;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void shouldValidateBookSuccessfully() {
        Book book = new Book("B001", "Clean Code", "Martin", true);
        assertDoesNotThrow(() -> BookValidator.validate(book));
    }

    @Test
    void shouldFailValidationForBookWithNullTitle() {
        Book book = new Book("B001", null, "Martin", true);
        assertThrows(IllegalArgumentException.class, () -> BookValidator.validate(book));
    }

    @Test
    void shouldValidateUserSuccessfully() {
        User user = new User("U001", "Carlos");
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void shouldFailValidationForUserWithNullName() {
        User user = new User("U001", null);
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validate(user));
    }
}