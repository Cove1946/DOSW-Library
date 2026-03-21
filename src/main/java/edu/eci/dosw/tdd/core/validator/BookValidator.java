package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import org.springframework.stereotype.Component;

@Component
public class BookValidator {

    public void validate(Book book) {
        ValidationUtil.validateNotNull(book, "El libro no puede ser nulo");
        ValidationUtil.validateNotBlank(book.getId(), "El ID del libro no puede estar vacío");
        ValidationUtil.validateNotBlank(book.getTitle(), "El título no puede estar vacío"); // ← esta línea
        ValidationUtil.validateNotBlank(book.getAuthor(), "El autor no puede estar vacío");
    }

    public void validateCopies(int copies){
        ValidationUtil.validatePositive(copies, "Las copias deben ser mayor a 0");
    }

}