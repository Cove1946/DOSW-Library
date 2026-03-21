package edu.eci.dosw.tdd.core.service;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private final UserValidator userValidator;

    public UserService(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    public void registerUser(User user) {
        userValidator.validate(user);
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
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    public void updateUser(String id, User updatedUser) {
        ValidationUtil.validateNotBlank(id, "El ID del usuario no puede estar vacío");
        userValidator.validate(updatedUser);
        User user = getUserById(id);
        user.setName(updatedUser.getName());
    }

    public void deleteUser(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del usuario no puede estar vacío");
        User user = getUserById(id);
        users.remove(user);
    }
}