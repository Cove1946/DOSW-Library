package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(new UserValidator());
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        userService.registerUser(new User("U001", "Carlos"));
        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void shouldGetAllUsers() {
        userService.registerUser(new User("U001", "Carlos"));
        userService.registerUser(new User("U002", "Maria"));
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        userService.registerUser(new User("U001", "Carlos"));
        User user = userService.getUserById("U001");
        assertEquals("Carlos", user.getName());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById("NONEXISTENT"));
    }
}