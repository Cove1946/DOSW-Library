package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import edu.eci.dosw.tdd.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user-1", "Juan Pérez", "juanp",
                "password123", Role.REGULAR_USER,
                "juan@mail.com", null, null);
    }

    // --- registerUser ---

    @Test
    void registerUser_deberiaGuardarCuandoDatosValidos() {
        doNothing().when(userValidator).validate(any());
        when(userRepository.existsByUsername("juanp")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");

        userService.registerUser(user);

        verify(userRepository).save(user);
        assertEquals("hashed", user.getPassword());
    }

    @Test
    void registerUser_deberiaGenerarIdSiNoTiene() {
        user.setId(null);
        doNothing().when(userValidator).validate(any());
        when(userRepository.existsByUsername("juanp")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");

        userService.registerUser(user);

        assertNotNull(user.getId());
    }

    @Test
    void registerUser_deberiaLanzarExcepcionSiUsernameYaExiste() {
        doNothing().when(userValidator).validate(any());
        when(userRepository.existsByUsername("juanp")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(user));
        verify(userRepository, never()).save(any());
    }

    // --- login ---

    @Test
    void login_deberiaRetornarUsuarioCuandoCredencialesValidas() {
        when(userRepository.findByUsername("juanp")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        User result = userService.login("juanp", "password123");

        assertEquals("juanp", result.getUsername());
    }

    @Test
    void login_deberiaLanzarExcepcionCuandoPasswordIncorrecto() {
        when(userRepository.findByUsername("juanp")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.login("juanp", "wrongpass"));
    }

    @Test
    void login_deberiaLanzarExcepcionSiUsernameEsBlanco() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.login("", "password123"));
    }

    // --- getAllUsers ---

    @Test
    void getAllUsers_deberiaRetornarLista() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
    }

    // --- getUsersByRole ---

    @Test
    void getUsersByRole_deberiaRetornarUsuariosPorRol() {
        when(userRepository.findByRole("REGULAR_USER")).thenReturn(List.of(user));

        List<User> result = userService.getUsersByRole(Role.REGULAR_USER);

        assertEquals(1, result.size());
    }

    @Test
    void getUsersByRole_deberiaLanzarExcepcionSiRolEsNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.getUsersByRole(null));
    }

    // --- getUserById ---

    @Test
    void getUserById_deberiaRetornarUsuarioCuandoExiste() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        User result = userService.getUserById("user-1");

        assertEquals("user-1", result.getId());
    }

    @Test
    void getUserById_deberiaLanzarExcepcionCuandoNoExiste() {
        when(userRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById("no-existe"));
    }

    // --- updateUser ---

    @Test
    void updateUser_deberiaActualizarCuandoExiste() {
        doNothing().when(userValidator).validate(any());
        when(userRepository.existsById("user-1")).thenReturn(true);

        userService.updateUser("user-1", user);

        verify(userRepository).save(user);
    }

    @Test
    void updateUser_deberiaLanzarExcepcionCuandoNoExiste() {
        doNothing().when(userValidator).validate(any());
        when(userRepository.existsById("no-existe")).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser("no-existe", user));
    }

    // --- deleteUser ---

    @Test
    void deleteUser_deberiaEliminarCuandoExiste() {
        when(userRepository.existsById("user-1")).thenReturn(true);

        userService.deleteUser("user-1");

        verify(userRepository).delete("user-1");
    }

    @Test
    void deleteUser_deberiaLanzarExcepcionCuandoNoExiste() {
        when(userRepository.existsById("no-existe")).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser("no-existe"));
    }

    // --- getUserByUsername ---

    @Test
    void getUserByUsername_deberiaRetornarUsuario() {
        when(userRepository.findByUsername("juanp")).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername("juanp");

        assertEquals("juanp", result.getUsername());
    }

    @Test
    void getUserByUsername_deberiaLanzarExcepcionSiNoExiste() {
        when(userRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUserByUsername("noexiste"));
    }
}