package edu.eci.dosw.tdd.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(
        @NotBlank(message = "El ID no puede estar vacío") String id,
        @NotBlank(message = "El nombre no puede estar vacío") String name
) {}

