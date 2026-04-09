package edu.eci.dosw.tdd.core.validator;

import edu.eci.dosw.tdd.core.exception.InvalidBookDataException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import org.springframework.stereotype.Component;

@Component
public class BookValidator {

    public void validate(Book book) {
        ValidationUtil.validateNotNull(book, "El libro no puede ser nulo");
        ValidationUtil.validateNotBlank(book.getId(), "El ID del libro no puede estar vacío");
        ValidationUtil.validateNotBlank(book.getTitle(), "El título no puede estar vacío");
        ValidationUtil.validateNotBlank(book.getAuthor(), "El autor no puede estar vacío");
    }

    public void validateCopies(int copies){
        ValidationUtil.validatePositive(copies, "Las copias deben ser mayor a 0");
    }

    public void validateStock(int totalCopies, int availableCopies){
        if (totalCopies <= 0){
            throw new InvalidBookDataException("La cantidad total de ejemplares debe ser mayor a 0");
        }
        if (availableCopies < 0){
            throw new InvalidBookDataException("La cantidad de ejemplares disponibles no puede ser negativa");
        }
        if (availableCopies > totalCopies){
            throw new InvalidBookDataException("Los ejemplares disponibles no puede superar el total de copias");
        }
    }

}