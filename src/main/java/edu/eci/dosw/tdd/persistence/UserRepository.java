package edu.eci.dosw.tdd.persistence;

import edu.eci.dosw.tdd.core.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String id);
    List<User> findAll();
    void delete(String id);
    boolean existsById(String id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findByRole(String role);
}