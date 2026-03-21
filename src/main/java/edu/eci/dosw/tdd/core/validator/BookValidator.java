package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import org.springframework.stereotype.Component;

@Component
public class BookValidator {

    public void validate(Book book) {
        ValidationUtil.validateNotNull(book, "El libro no puede ser nulo");
        ValidationUtil.validateNotNull(book.getId(), "El ID del libro no puede estar vacio");
        ValidationUtil.validateNotNull(book.getTitle(), "El titulo no puede estar vacio");
        ValidationUtil.validateNotNull(book.getAuthor(), "El autor no puede estar vacio");
    }

    public void validateCopies(int copies){
        ValidationUtil.validatePositive(copies, "Las copias deben ser mayor a 0");
    }

}