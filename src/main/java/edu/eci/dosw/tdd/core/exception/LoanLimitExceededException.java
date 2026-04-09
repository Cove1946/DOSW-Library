package edu.eci.dosw.tdd.core.exception;

public class LoanLimitExceededException extends RuntimeException {
    public LoanLimitExceededException(String message) {
        super(message);
    }
}