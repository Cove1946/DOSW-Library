package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.validator.LoanValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanValidatorTest {

    private LoanValidator loanValidator;

    @BeforeEach
    void setUp() {
        loanValidator = new LoanValidator();
    }


    @Test
    void shouldPassValidationForValidLoanRequest() {
        assertDoesNotThrow(() -> loanValidator.validateLoanRequest("B001", "U001"));
    }


    @Test
    void shouldThrowWhenBookIdIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> loanValidator.validateLoanRequest("", "U001"));
    }

    @Test
    void shouldThrowWhenBookIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> loanValidator.validateLoanRequest(null, "U001"));
    }

    @Test
    void shouldThrowWhenUserIdIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> loanValidator.validateLoanRequest("B001", ""));
    }

    @Test
    void shouldThrowWhenUserIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> loanValidator.validateLoanRequest("B001", null));
    }
}