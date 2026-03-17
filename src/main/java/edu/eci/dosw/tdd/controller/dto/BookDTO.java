package edu.eci.dosw.tdd.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record BookDTO(
        @NotBlank(message = "El ID no puede estar vacío") String id,
        @NotBlank(message = "El título no puede estar vacío") String title,
        @NotBlank(message = "El autor no puede estar vacío") String author
) {}

