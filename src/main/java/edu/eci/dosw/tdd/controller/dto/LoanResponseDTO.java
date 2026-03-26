package edu.eci.dosw.tdd.controller.dto;

import edu.eci.dosw.tdd.core.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDTO {
    private String bookId;
    private String bookTitle;
    private String userId;
    private String username;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private Status status;
}