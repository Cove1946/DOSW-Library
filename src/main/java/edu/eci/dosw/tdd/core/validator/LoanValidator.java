package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanAlreadyReturnedException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import org.springframework.stereotype.Component;

@Component
public class LoanValidator {
    public void validateIds(String bookId, String userId) {
        ValidationUtil.validateNotBlank(bookId, "El ID del libro no puede estar vacío");
        ValidationUtil.validateNotBlank(userId, "El ID del usuario no puede estar vacío");
    }

    public void validateBookAvailable(int availableCopies) {
        if (availableCopies <= 0) {
            throw new BookNotAvailableException("No hay copias disponibles para el libro solicitado");
        }
    }

    public void validateActiveLoan(Loan loan){
        if (loan.getStatus() == Status.RETURNED){
            throw new LoanAlreadyReturnedException("El prestamo del libro ya fue devuelto anteriormente");
        }
    }
}