package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.ValidationUtil;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.mapper.BookPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookPersistenceMapper bookMapper;
    private final BookValidator bookValidator;

    public BookService(BookRepository bookRepository,
                       BookPersistenceMapper bookMapper,
                       BookValidator bookValidator) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookValidator = bookValidator;
    }

    public void addBook(Book book) {
        bookValidator.validate(book);
        bookValidator.validateStock(book.getTotalCopies(), book.getAvailableCopies());
        bookRepository.save(bookMapper.toEntity(book));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toModel)
                .collect(Collectors.toList());
    }

    public Book getBookById(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del libro no puede estar vacío");
        return bookRepository.findById(id)
                .map(bookMapper::toModel)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado con ID: " + id));
    }

    public int getCopies(String id) {
        return getBookById(id).getAvailableCopies();
    }

    public void decreaseAvailableCopies(String id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado con ID: " + id));
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No hay copias disponibles para el libro: " + id);
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
    }

    public void increaseAvailableCopies(String id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado con ID: " + id));
        if (book.getAvailableCopies() >= book.getTotalCopies()) {
            throw new IllegalStateException(
                    "Las copias disponibles ya están al máximo (" + book.getTotalCopies() + ") para el libro: " + id
            );
        }
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
    }

    public void updateTotalCopies(String id, int newTotal) {
        ValidationUtil.validateNotBlank(id, "El ID del libro no puede estar vacío");
        if (newTotal <= 0) {
            throw new IllegalArgumentException("El total de ejemplares debe ser mayor a 0");
        }
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado con ID: " + id));
        book.setTotalCopies(newTotal);
        if (book.getAvailableCopies() > newTotal) {
            book.setAvailableCopies(newTotal);
        }
        bookRepository.save(book);
    }

    public void deleteBook(String id) {
        ValidationUtil.validateNotBlank(id, "El ID del libro no puede estar vacío");
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Libro no encontrado con ID: " + id);
        }
        bookRepository.deleteById(id);
    }
}