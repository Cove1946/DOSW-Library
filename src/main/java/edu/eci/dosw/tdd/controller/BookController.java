package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PostMapping
    public ResponseEntity<Void> addBook(@RequestBody BookDTO bookDTO) {
        Book book = bookMapper.toModel(bookDTO);
        bookService.addBook(book, bookDTO.getCopies());
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(@PathVariable String id,
                                                   @RequestParam boolean available) {
        bookService.updateBookAvailability(id, available);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}