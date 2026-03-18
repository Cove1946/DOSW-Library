package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();


    public void registerUser(User user) {
        users.add(user);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(String id) throws UserNotFoundException {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    public void updateUser(String id, User updatedUser) throws UserNotFoundException {
        User user = getUserById(id);
        user.setName(updatedUser.getName());
    }

    public void deleteUser(String id) throws UserNotFoundException {
        User user = getUserById(id);
        users.remove(user);
    }


}