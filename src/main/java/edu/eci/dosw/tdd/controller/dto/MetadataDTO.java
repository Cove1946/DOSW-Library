package edu.eci.dosw.tdd.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetadataDTO {
    private int pages;
    private String language;
    private String publisher;
}