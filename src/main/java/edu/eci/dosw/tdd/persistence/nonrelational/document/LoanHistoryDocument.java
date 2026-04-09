package edu.eci.dosw.tdd.persistence.nonrelational.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanHistoryDocument {
    private String status;
    private LocalDate executionDate;
}