package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.LoanResponseDTO;
import edu.eci.dosw.tdd.core.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanResponseDTO toDTO(Loan loan) {
        return new LoanResponseDTO(
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getUser().getId(),
                loan.getUser().getUsername(),
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.getStatus()
        );
    }
}