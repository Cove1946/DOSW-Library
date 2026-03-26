package edu.eci.dosw.tdd.core.service;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private final UserValidator userValidator;

    public UserService(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    public void registerUser(User user) {
        userValidator.validate(user);
        boolean usernameExists = users.stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(user.getUsername()));
        if (usernameExists) {
            throw new IllegalArgumentException("El nombre de usuario ya esta en uso");
        }
        users.add(user);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del usuario no puede estar vacío");
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID"));
    }

    public void updateUser(String id, User updatedUser) {
        ValidationUtil.validateNotBlank(id, "El ID del usuario no puede estar vacío");
        userValidator.validate(updatedUser);
        User user = getUserById(id);
        user.setName(updatedUser.getName());
        user.setUsername(updatedUser.getUsername());
        user.setPassword(updatedUser.getPassword());
        user.setRole(updatedUser.getRole());
    }

    public void deleteUser(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del usuario no puede estar vacío");
        User user = getUserById(id);
        users.remove(user);
    }

    public User login(String username, String password) {
        ValidationUtil.validateNotBlank(username, "El nombre de usuario no puede estar vacio");
        ValidationUtil.validateNotBlank(password, "La contraseña no puede estar vacia");
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
    }

    public List<User> getUsersByRole(Role role) {
        ValidationUtil.validateNotNull(role, "El rol no puede ser nulo");
        return users.stream()
                .filter(u -> u.getRole() == role)
                .collect(Collectors.toList());
    }
}