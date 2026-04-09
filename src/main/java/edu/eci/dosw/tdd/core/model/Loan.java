package edu.eci.dosw.tdd.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Loan {
    private String id;
    private Book book;
    private User user;
    private LocalDate loanDate;
    private Status status;
    private LocalDate returnDate;

    private List<LoanHistory> history;
}

