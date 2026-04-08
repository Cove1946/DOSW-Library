package edu.eci.dosw.tdd.persistence.nonrelational.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDocument {
    private String id;
    private String bookId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private String status;
    private List<LoanHistoryDocument> history;
}