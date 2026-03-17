package edu.eci.dosw.tdd.core.validator;

import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateUser(edu.eci.dosw.tdd.core.model.User user) {
        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (user.getId() == null || user.getId().isBlank()) {
            throw new IllegalArgumentException("El ID del usuario no puede estar vacío.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario no puede estar vacío.");
        }
    }
}