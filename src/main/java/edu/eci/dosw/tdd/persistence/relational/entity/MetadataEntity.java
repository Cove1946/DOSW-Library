package edu.eci.dosw.tdd.persistence.relational.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MetadataEntity {
    private int pages;
    private String language;
    private String publisher;
}