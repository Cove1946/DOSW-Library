package edu.eci.dosw.tdd.core.validator;

import org.springframework.stereotype.Component;

@Component
public class LoanValidator {

    public void validateLoanRequest(String bookId, String userId) {
        if (bookId == null || bookId.isBlank()) {
            throw new IllegalArgumentException("El ID del libro no puede estar vacío.");
        }
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("El ID del usuario no puede estar vacío.");
        }
    }
}