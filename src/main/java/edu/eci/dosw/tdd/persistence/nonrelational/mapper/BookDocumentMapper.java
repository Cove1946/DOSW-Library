package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Metadata;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.document.MetadataDocument;
import org.springframework.stereotype.Component;

@Component
public class BookDocumentMapper {

    public Book toModel(BookDocument doc) {
        if (doc == null) return null;

        Metadata metadata = null;
        if (doc.getMetadata() != null) {
            metadata = new Metadata(
                    doc.getMetadata().getPages(),
                    doc.getMetadata().getLanguage(),
                    doc.getMetadata().getPublisher()
            );
        }

        return new Book(
                doc.getId(),
                doc.getTitle(),
                doc.getAuthor(),
                doc.getTotalCopies(),
                doc.getAvailableCopies(),
                doc.getCategories(),
                doc.getPublicationType(),
                doc.getPublicationDate(),
                doc.getIsbn(),
                metadata,
                doc.getAvailabilityStatus(),
                doc.getBorrowedCopies(),
                doc.getAddedToCatalogDate()
        );
    }

    public BookDocument toDocument(Book model) {
        if (model == null) return null;

        MetadataDocument metadataDoc = null;
        if (model.getMetadata() != null) {
            metadataDoc = new MetadataDocument(
                    model.getMetadata().getPages(),
                    model.getMetadata().getLanguage(),
                    model.getMetadata().getPublisher()
            );
        }

        return new BookDocument(
                model.getId(),
                model.getTitle(),
                model.getAuthor(),
                model.getTotalCopies(),
                model.getAvailableCopies(),
                model.getCategories(),
                model.getPublicationType(),
                model.getPublicationDate(),
                model.getIsbn(),
                metadataDoc,
                model.getAvailabilityStatus(),
                model.getBorrowedCopies(),
                model.getAddedToCatalogDate()
        );
    }
}