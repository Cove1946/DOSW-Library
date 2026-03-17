package edu.eci.dosw.tdd.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LoanDTO(
        @NotBlank(message = "El ID del libro no puede estar vacío") String bookId,
        @NotBlank(message = "El ID del usuario no puede estar vacío") String userId
) {}

