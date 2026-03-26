package edu.eci.dosw.tdd.core.exception;

public class LoanAlreadyReturnedException extends RuntimeException{
    public LoanAlreadyReturnedException(String message){
        super(message);
    }
}
