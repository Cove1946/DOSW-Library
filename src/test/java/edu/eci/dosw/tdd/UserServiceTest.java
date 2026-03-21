package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(new UserValidator());
    }

    // ─── EXITOSOS ─────────────────────────────────────────

    @Test
    void registerUser_shouldRegisterSuccessfully() {
        User user = new User("U1", "Lees");
        userService.registerUser(user);
        assertEquals(user, userService.getUserById("U1"));
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        userService.registerUser(new User("U1", "Lees"));
        userService.registerUser(new User("U2", "Juan"));
        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void updateUser_shouldUpdateNameCorrectly() {
        userService.registerUser(new User("U1", "Lees"));
        userService.updateUser("U1", new User("U1", "Lees Updated"));
        assertEquals("Lees Updated", userService.getUserById("U1").getName());
    }

    @Test
    void deleteUser_shouldRemoveUserFromList() {
        userService.registerUser(new User("U1", "Lees"));
        userService.deleteUser("U1");
        assertEquals(0, userService.getAllUsers().size());
    }

    // ─── ERROR ────────────────────────────────────────────

    @Test
    void registerUser_shouldThrowException_whenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser(null)
        );
    }

    @Test
    void registerUser_shouldThrowException_whenNameIsBlank() {
        User user = new User("U1", "");
        assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser(user)
        );
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        assertThrows(UserNotFoundException.class, () ->
                userService.getUserById("U99")
        );
    }
}