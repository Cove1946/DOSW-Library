package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Books", description = "Operaciones para gestión de libros")
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @Operation(summary = "Agregar un libro", description = "Agrega un nuevo libro al sistema con su cantidad de copias disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del libro inválidos")
    })
    @PostMapping
    public ResponseEntity<Void> addBook(@RequestBody BookDTO bookDTO) {
        Book book = bookMapper.toModel(bookDTO);
        bookService.addBook(book, bookDTO.getCopies());
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Obtener todos los libros", description = "Retorna la lista completa de libros registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de libros retornada exitosamente")
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Operation(summary = "Obtener libro por ID", description = "Busca y retorna un libro específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "500", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Actualizar disponibilidad de un libro", description = "Cambia el estado de disponibilidad de un libro por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada exitosamente"),
            @ApiResponse(responseCode = "500", description = "Libro no encontrado")
    })
    @PutMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(@PathVariable String id,
                                                   @RequestParam boolean available) {
        bookService.updateBookAvailability(id, available);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar un libro", description = "Elimina un libro del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Libro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}