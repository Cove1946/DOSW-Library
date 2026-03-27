package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.LoanResponseDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Loans", description = "Operaciones para gestión de préstamos")
@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    public LoanController(LoanService loanService, LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }

    @Operation(summary = "Crear un préstamo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Préstamo creado exitosamente"),
            @ApiResponse(responseCode = "409", description = "No hay copias disponibles"),
            @ApiResponse(responseCode = "404", description = "Libro o usuario no encontrado")
    })
    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestParam String bookId,
                                                      @RequestParam String userId) {
        return ResponseEntity.status(201).body(loanMapper.toDTO(loanService.createLoan(bookId, userId)));
    }

    @Operation(summary = "Devolver un libro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro devuelto exitosamente"),
            @ApiResponse(responseCode = "409", description = "El préstamo ya fue devuelto"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    })
    @PutMapping("/return")
    public ResponseEntity<LoanResponseDTO> returnBook(@RequestParam String bookId,
                                                      @RequestParam String userId) {
        return ResponseEntity.ok(loanMapper.toDTO(loanService.returnBook(bookId, userId)));
    }

    @Operation(summary = "Obtener todos los préstamos")
    @ApiResponse(responseCode = "200", description = "Lista de préstamos retornada exitosamente")
    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> getAllLoans() {
        List<LoanResponseDTO> result = loanService.getAllLoans()
                .stream()
                .map(loanMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Obtener préstamos por usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamos retornados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanResponseDTO>> getLoansByUser(@PathVariable String userId) {
        List<LoanResponseDTO> result = loanService.getLoansByUser(userId)
                .stream()
                .map(loanMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Obtener préstamos por libro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamos retornados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<LoanResponseDTO>> getLoansByBook(@PathVariable String bookId) {
        List<LoanResponseDTO> result = loanService.getLoansByBook(bookId)
                .stream()
                .map(loanMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Expirar un préstamo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamo expirado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Préstamo activo no encontrado")
    })
    @PutMapping("/expire")
    public ResponseEntity<LoanResponseDTO> expireLoan(@RequestParam String bookId,
                                                      @RequestParam String userId) {
        return ResponseEntity.ok(loanMapper.toDTO(loanService.expireLoan(bookId, userId)));
    }
}