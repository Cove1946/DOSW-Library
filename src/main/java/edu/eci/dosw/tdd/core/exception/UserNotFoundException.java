package edu.eci.dosw.tdd.core.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("Usuario con ID " + userId + " no encontrado.");
    }
}
