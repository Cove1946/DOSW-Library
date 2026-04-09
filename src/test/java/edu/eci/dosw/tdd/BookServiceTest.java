package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.exception.InvalidBookDataException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import edu.eci.dosw.tdd.persistence.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookValidator bookValidator;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book("book-1", "Clean Code", "Robert Martin",
                5, 5, List.of("Programación"), "DIGITAL",
                null, "978-0132350884", null,
                "AVAILABLE", 0, null);
    }

    // --- addBook ---

    @Test
    void addBook_deberiaGuardarCuandoDatosValidos() {
        doNothing().when(bookValidator).validate(any());
        doNothing().when(bookValidator).validateStock(anyInt(), anyInt());

        bookService.addBook(book);

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void addBook_deberiaGenerarIdSiNoTiene() {
        book.setId(null);
        doNothing().when(bookValidator).validate(any());
        doNothing().when(bookValidator).validateStock(anyInt(), anyInt());

        bookService.addBook(book);

        assertNotNull(book.getId());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void addBook_deberiaLanzarExcepcionSiStockInvalido() {
        doNothing().when(bookValidator).validate(any());
        doThrow(new InvalidBookDataException("Stock inválido"))
                .when(bookValidator).validateStock(anyInt(), anyInt());

        assertThrows(InvalidBookDataException.class, () -> bookService.addBook(book));
        verify(bookRepository, never()).save(any());
    }

    // --- getAllBooks ---

    @Test
    void getAllBooks_deberiaRetornarListaCompleta() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<Book> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
    }

    // --- getBookById ---

    @Test
    void getBookById_deberiaRetornarLibroCuandoExiste() {
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));

        Book result = bookService.getBookById("book-1");

        assertEquals("book-1", result.getId());
    }

    @Test
    void getBookById_deberiaLanzarExcepcionCuandoNoExiste() {
        when(bookRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById("no-existe"));
    }

    @Test
    void getBookById_deberiaLanzarExcepcionSiIdEsBlanco() {
        assertThrows(IllegalArgumentException.class, () -> bookService.getBookById(""));
    }

    // --- getCopies ---

    @Test
    void getCopies_deberiaRetornarCopiesDisponibles() {
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));

        int copies = bookService.getCopies("book-1");

        assertEquals(5, copies);
    }

    // --- decreaseAvailableCopies ---

    @Test
    void decreaseAvailableCopies_deberiaReducirCopias() {
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));

        bookService.decreaseAvailableCopies("book-1");

        assertEquals(4, book.getAvailableCopies());
        verify(bookRepository).save(book);
    }

    @Test
    void decreaseAvailableCopies_deberiaLanzarExcepcionSiNoCopias() {
        book.setAvailableCopies(0);
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));

        assertThrows(IllegalStateException.class,
                () -> bookService.decreaseAvailableCopies("book-1"));
    }

    // --- increaseAvailableCopies ---

    @Test
    void increaseAvailableCopies_deberiaAumentarCopias() {
        book.setAvailableCopies(3);
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));

        bookService.increaseAvailableCopies("book-1");

        assertEquals(4, book.getAvailableCopies());
        verify(bookRepository).save(book);
    }

    @Test
    void increaseAvailableCopies_deberiaLanzarExcepcionSiYaEstaAlMaximo() {
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));

        assertThrows(IllegalStateException.class,
                () -> bookService.increaseAvailableCopies("book-1"));
    }

    // --- updateTotalCopies ---

    @Test
    void updateTotalCopies_deberiaActualizarTotal() {
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));

        bookService.updateTotalCopies("book-1", 10);

        assertEquals(10, book.getTotalCopies());
        verify(bookRepository).save(book);
    }

    @Test
    void updateTotalCopies_deberiaAjustarDisponiblesAlBajarTotal() {
        book.setAvailableCopies(5);
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));

        bookService.updateTotalCopies("book-1", 3);

        assertEquals(3, book.getTotalCopies());
        assertEquals(3, book.getAvailableCopies());
    }

    @Test
    void updateTotalCopies_deberiaLanzarExcepcionSiNuevoTotalEsCero() {
        assertThrows(IllegalArgumentException.class,
                () -> bookService.updateTotalCopies("book-1", 0));
    }

    // --- deleteBook ---

    @Test
    void deleteBook_deberiaEliminarLibro() {
        when(bookRepository.existsById("book-1")).thenReturn(true);

        bookService.deleteBook("book-1");

        verify(bookRepository).delete("book-1");
    }

    @Test
    void deleteBook_deberiaLanzarExcepcionSiNoExiste() {
        when(bookRepository.existsById("no-existe")).thenReturn(false);

        assertThrows(BookNotFoundException.class,
                () -> bookService.deleteBook("no-existe"));
    }
}