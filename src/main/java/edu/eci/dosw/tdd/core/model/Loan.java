package edu.eci.dosw.tdd.core.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Loan {

    private Book book;
    private User user;
    private LocalDate loanDate;
    private String status;
    private LocalDate returnDate;
}
