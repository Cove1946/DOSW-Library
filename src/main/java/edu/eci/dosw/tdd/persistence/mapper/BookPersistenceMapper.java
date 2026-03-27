package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import org.springframework.stereotype.Component;

@Component
public class BookPersistenceMapper {

    public Book toModel(BookEntity entity) {
        if (entity == null) return null;
        return new Book(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getTotalCopies(),
                entity.getAvailableCopies()
        );
    }

    public BookEntity toEntity(Book model) {
        if (model == null) return null;
        return new BookEntity(
                model.getId(),
                model.getTitle(),
                model.getAuthor(),
                model.getTotalCopies(),
                model.getAvailableCopies()
        );
    }
}