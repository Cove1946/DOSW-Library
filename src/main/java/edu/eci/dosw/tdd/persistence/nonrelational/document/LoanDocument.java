package edu.eci.dosw.tdd.persistence.nonrelational.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "loans")
public class LoanDocument {

    @Id
    private String id;
    private String bookId;
    private String userId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private String status;
    private List<LoanHistoryDocument> history;
}