package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.util.ValidationUtil;
import org.springframework.stereotype.Component;

@Component
public class LoanValidator {
    public void validateIds(String bookId, String userId) {
        ValidationUtil.validateNotBlank(bookId, "El ID del libro no puede estar vacío");
        ValidationUtil.validateNotBlank(userId, "El ID del usuario no puede estar vacío");
    }
}