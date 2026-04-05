package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Metadata;
import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.relational.entity.MetadataEntity;
import org.springframework.stereotype.Component;

@Component
public class BookPersistenceMapper {

    public Book toModel(BookEntity entity) {
        if (entity == null) return null;

        Metadata metadata = null;
        if (entity.getMetadata() != null) {
            metadata = new Metadata(
                    entity.getMetadata().getPages(),
                    entity.getMetadata().getLanguage(),
                    entity.getMetadata().getPublisher()
            );
        }

        return new Book(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getTotalCopies(),
                entity.getAvailableCopies(),
                entity.getCategories(),
                entity.getPublicationType(),
                entity.getPublicationDate(),
                entity.getIsbn(),
                metadata,
                entity.getAvailabilityStatus(),
                entity.getBorrowedCopies(),
                entity.getAddedToCatalogDate()
        );
    }

    public BookEntity toEntity(Book model) {
        if (model == null) return null;

        MetadataEntity metadataEntity = null;
        if (model.getMetadata() != null) {
            metadataEntity = new MetadataEntity(
                    model.getMetadata().getPages(),
                    model.getMetadata().getLanguage(),
                    model.getMetadata().getPublisher()
            );
        }

        return new BookEntity(
                model.getId(),
                model.getTitle(),
                model.getAuthor(),
                model.getTotalCopies(),
                model.getAvailableCopies(),
                model.getCategories(),
                model.getPublicationType(),
                model.getPublicationDate(),
                model.getIsbn(),
                metadataEntity,
                model.getAvailabilityStatus(),
                model.getBorrowedCopies(),
                model.getAddedToCatalogDate()
        );
    }
}