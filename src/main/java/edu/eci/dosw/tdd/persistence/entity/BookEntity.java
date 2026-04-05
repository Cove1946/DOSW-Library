package edu.eci.dosw.tdd.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @Column(name = "book_id")
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "total_copies", nullable = false)
    private int totalCopies;

    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

    @ElementCollection
    @CollectionTable(name = "book_categories", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "category")
    private List<String> categories;

    @Column(name = "publication_type")
    private String publicationType;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "isbn", unique = true)
    private String isbn;

    @Embedded
    private MetadataEntity metadata;

    @Column(name = "availability_status")
    private String availabilityStatus;

    @Column(name = "borrowed_copies")
    private int borrowedCopies;

    @Column(name = "added_to_catalog_date")
    private LocalDate addedToCatalogDate;
}