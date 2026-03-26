package edu.eci.dosw.tdd.core.exception;

public class InvalidBookDataException extends RuntimeException{
    public InvalidBookDataException(String messenge){
        super(messenge);
    }
}
