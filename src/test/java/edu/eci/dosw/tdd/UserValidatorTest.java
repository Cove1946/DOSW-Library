package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
    }


    @Test
    void shouldPassValidationForValidUser() {
        User user = new User("U001", "Carlos");
        assertDoesNotThrow(() -> userValidator.validateUser(user));
    }


    @Test
    void shouldThrowWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> userValidator.validateUser(null));
    }

    @Test
    void shouldThrowWhenUserIdIsBlank() {
        User user = new User("", "Carlos");
        assertThrows(IllegalArgumentException.class,
                () -> userValidator.validateUser(user));
    }

    @Test
    void shouldThrowWhenUserNameIsBlank() {
        User user = new User("U001", "");
        assertThrows(IllegalArgumentException.class,
                () -> userValidator.validateUser(user));
    }
}