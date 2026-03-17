package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<String> addBook(
            @Valid @RequestBody BookDTO dto,
            @RequestParam(defaultValue = "1") int copies) {
        Book book = new Book(dto.id(), dto.title(), dto.author(), true);
        bookService.addBook(book, copies);
        return ResponseEntity.status(HttpStatus.CREATED).body("Libro agregado exitosamente.");
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<String> updateAvailability(
            @PathVariable String id,
            @RequestParam boolean available) {
        bookService.updateAvailability(id, available);
        return ResponseEntity.ok("Disponibilidad actualizada.");
    }
}