package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import edu.eci.dosw.tdd.persistence.mapper.UserPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userMapper;
    private final UserValidator userValidator;

    public UserService(UserRepository userRepository,
                       UserPersistenceMapper userMapper,
                       UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
    }

    public void registerUser(User user) {
        userValidator.validate(user);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso: " + user.getUsername());
        }
        userRepository.save(userMapper.toEntity(user));
    }

    public User login(String username, String password) {
        ValidationUtil.validateNotBlank(username, "El nombre de usuario no puede estar vacío");
        ValidationUtil.validateNotBlank(password, "La contraseña no puede estar vacía");
        return userRepository.findByUsernameAndPassword(username, password)
                .map(userMapper::toModel)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<User> getUsersByRole(Role role) {
        ValidationUtil.validateNotNull(role, "El rol no puede ser nulo");
        return userRepository.findByRole(role.name())
                .stream()
                .map(userMapper::toModel)
                .collect(Collectors.toList());
    }

    public User getUserById(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del usuario no puede estar vacío");
        return userRepository.findById(id)
                .map(userMapper::toModel)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));
    }

    public void updateUser(String id, User updatedUser) {
        ValidationUtil.validateNotBlank(id, "El ID del usuario no puede estar vacío");
        userValidator.validate(updatedUser);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.save(userMapper.toEntity(updatedUser));
    }

    public void deleteUser(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del usuario no puede estar vacío");
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}