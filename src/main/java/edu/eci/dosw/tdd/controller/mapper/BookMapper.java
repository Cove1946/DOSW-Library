package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.dto.MetadataDTO;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Metadata;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toModel(BookDTO dto) {

        Metadata metadata = null;
        if (dto.getMetadata() != null) {
            metadata = new Metadata(
                    dto.getMetadata().getPages(),
                    dto.getMetadata().getLanguage(),
                    dto.getMetadata().getPublisher()
            );
        }

        return new Book(
                dto.getId(),
                dto.getTitle(),
                dto.getAuthor(),
                dto.getTotalCopies(),
                dto.getAvailableCopies(),
                dto.getCategories(),
                dto.getPublicationType(),
                dto.getPublicationDate(),
                dto.getIsbn(),
                metadata,
                dto.getAvailabilityStatus(),
                dto.getBorrowedCopies(),
                dto.getAddedToCatalogDate()
        );
    }

    public BookDTO toDTO(Book book) {

        MetadataDTO metadataDTO = null;
        if (book.getMetadata() != null) {
            metadataDTO = new MetadataDTO(
                    book.getMetadata().getPages(),
                    book.getMetadata().getLanguage(),
                    book.getMetadata().getPublisher()
            );
        }

        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getCategories(),
                book.getPublicationType(),
                book.getPublicationDate(),
                book.getIsbn(),
                metadataDTO,
                book.getAvailabilityStatus(),
                book.getBorrowedCopies(),
                book.getAddedToCatalogDate()
        );
    }
}