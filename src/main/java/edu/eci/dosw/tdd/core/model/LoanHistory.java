package edu.eci.dosw.tdd.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanHistory {
    private Status status;
    private LocalDate executionDate;
}