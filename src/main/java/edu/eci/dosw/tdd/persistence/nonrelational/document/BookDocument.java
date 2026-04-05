package edu.eci.dosw.tdd.persistence.nonrelational.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class BookDocument {

    @Id
    private String id;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;
    private List<String> categories;
    private String publicationType;
    private LocalDate publicationDate;
    private String isbn;
    private MetadataDocument metadata;
    private String availabilityStatus;
    private int borrowedCopies;
    private LocalDate addedToCatalogDate;
}