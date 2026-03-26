package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toModel(BookDTO dto) {
        return new Book(
                dto.getId(),
                dto.getTitle(),
                dto.getAuthor(),
                dto.getTotalCopies(),
                dto.getAvailableCopies()
        );
    }

    public BookDTO toDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        );
    }
}