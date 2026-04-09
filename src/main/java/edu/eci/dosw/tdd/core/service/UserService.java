package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import edu.eci.dosw.tdd.persistence.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserValidator userValidator,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(IdGeneratorUtil.generateId());
        }
        userValidator.validate(user);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso: " + user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User login(String username, String password) {
        ValidationUtil.validateNotBlank(username, "El nombre de usuario no puede estar vacío");
        ValidationUtil.validateNotBlank(password, "La contraseña no puede estar vacía");

        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(Role role) {
        ValidationUtil.validateNotNull(role, "El rol no puede ser nulo");
        return userRepository.findByRole(role.name());
    }

    public User getUserById(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del usuario no puede estar vacío");
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));
    }

    public void updateUser(String id, User updatedUser) {
        ValidationUtil.validateNotBlank(id, "El ID del usuario no puede estar vacío");
        userValidator.validate(updatedUser);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.save(updatedUser);
    }

    public void deleteUser(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del usuario no puede estar vacío");
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.delete(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        "Usuario no encontrado con username: " + username));
    }
}