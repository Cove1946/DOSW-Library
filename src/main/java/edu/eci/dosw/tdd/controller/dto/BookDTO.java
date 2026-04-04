package edu.eci.dosw.tdd.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private String id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 200, message = "El título no puede superar 200 caracteres")
    private String title;

    @NotBlank(message = "El autor no puede estar vacío")
    private String author;

    @Min(value = 1, message = "Debe haber al menos 1 copia")
    private int totalCopies;

    @Min(value = 0, message = "Las copias disponibles no pueden ser negativas")
    private int availableCopies;
}