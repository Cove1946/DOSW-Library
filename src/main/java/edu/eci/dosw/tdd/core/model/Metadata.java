package edu.eci.dosw.tdd.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metadata {
    private int pages;
    private String language;
    private String publisher;
}