package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.exception.LoanAlreadyReturnedException;
import edu.eci.dosw.tdd.core.model.*;
import edu.eci.dosw.tdd.core.service.LoanService;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.persistence.BookRepository;
import edu.eci.dosw.tdd.persistence.LoanRepository;
import edu.eci.dosw.tdd.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanValidator loanValidator;

    @InjectMocks
    private LoanService loanService;

    private Book book;
    private User user;
    private Loan loan;

    @BeforeEach
    void setUp() {
        book = new Book("book-1", "Clean Code", "Robert Martin",
                5, 3, List.of("Programación"), null,
                null, null, null, "AVAILABLE", 0, null);

        user = new User("user-1", "Juan", "juanp",
                "hashed", Role.REGULAR_USER, "juan@mail.com", null, null);

        loan = new Loan("loan-1", book, user, LocalDate.now(),
                Status.ACTIVE, null, new ArrayList<>());
    }

    // --- createLoan ---

    @Test
    void createLoan_deberiaCrearPrestamoCuandoDatosValidos() {
        doNothing().when(loanValidator).validateIds(anyString(), anyString());
        doNothing().when(loanValidator).validateBookAvailable(anyInt());
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(loanRepository.save(any())).thenReturn(loan);
        when(bookRepository.save(any())).thenReturn(book);

        Loan result = loanService.createLoan("book-1", "user-1");

        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(loanRepository).save(any());
    }

    @Test
    void createLoan_deberiaReducirCopiaDisponibleAlCrear() {
        doNothing().when(loanValidator).validateIds(anyString(), anyString());
        doNothing().when(loanValidator).validateBookAvailable(anyInt());
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(loanRepository.save(any())).thenReturn(loan);
        when(bookRepository.save(any())).thenReturn(book);

        loanService.createLoan("book-1", "user-1");

        assertEquals(2, book.getAvailableCopies());
    }

    @Test
    void createLoan_deberiaLanzarExcepcionSiLibroNoExiste() {
        doNothing().when(loanValidator).validateIds(anyString(), anyString());
        when(bookRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> loanService.createLoan("no-existe", "user-1"));
    }

    @Test
    void createLoan_deberiaLanzarExcepcionSiNoHayCopias() {
        doNothing().when(loanValidator).validateIds(anyString(), anyString());
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        doThrow(new BookNotAvailableException("Sin copias"))
                .when(loanValidator).validateBookAvailable(anyInt());

        assertThrows(BookNotAvailableException.class,
                () -> loanService.createLoan("book-1", "user-1"));
    }

    // --- returnBook ---

    @Test
    void returnBook_deberiaMarcarComoDevuelto() {
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        doNothing().when(loanValidator).validateActiveLoan(any());
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);
        when(loanRepository.save(any())).thenReturn(loan);

        Loan result = loanService.returnBook("loan-1");

        assertEquals(Status.RETURNED, result.getStatus());
        assertNotNull(result.getReturnDate());
    }

    @Test
    void returnBook_deberiaAumentarCopiaDisponibleAlDevolver() {
        book.setAvailableCopies(2);
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        doNothing().when(loanValidator).validateActiveLoan(any());
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);
        when(loanRepository.save(any())).thenReturn(loan);

        loanService.returnBook("loan-1");

        assertEquals(3, book.getAvailableCopies());
    }

    @Test
    void returnBook_deberiaLanzarExcepcionSiPrestamoNoExiste() {
        when(loanRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> loanService.returnBook("no-existe"));
    }

    @Test
    void returnBook_deberiaLanzarExcepcionSiYaFueDevuelto() {
        loan.setStatus(Status.RETURNED);
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        doThrow(new LoanAlreadyReturnedException("Ya devuelto"))
                .when(loanValidator).validateActiveLoan(any());

        assertThrows(LoanAlreadyReturnedException.class,
                () -> loanService.returnBook("loan-1"));
    }

    @Test
    void returnBook_deberiaAgregarEntradaEnHistorial() {
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        doNothing().when(loanValidator).validateActiveLoan(any());
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan result = loanService.returnBook("loan-1");

        assertTrue(result.getHistory().stream()
                .anyMatch(h -> h.getStatus() == Status.RETURNED));
    }

    // --- expireLoan ---

    @Test
    void expireLoan_deberiaMarcarComoExpirado() {
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan result = loanService.expireLoan("loan-1");

        assertEquals(Status.EXPIRED, result.getStatus());
    }

    // --- getAllLoans ---

    @Test
    void getAllLoans_deberiaRetornarTodosLosPreatmos() {
        when(loanRepository.findAll()).thenReturn(List.of(loan));
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        List<Loan> result = loanService.getAllLoans();

        assertEquals(1, result.size());
    }

    // --- getLoansByUser ---

    @Test
    void getLoansByUser_deberiaRetornarPrestamosPorUsuario() {
        when(userRepository.existsById("user-1")).thenReturn(true);
        when(loanRepository.findByUserId("user-1")).thenReturn(List.of(loan));
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        List<Loan> result = loanService.getLoansByUser("user-1");

        assertEquals(1, result.size());
    }

    @Test
    void getLoansByUser_deberiaLanzarExcepcionSiUsuarioNoExiste() {
        when(userRepository.existsById("no-existe")).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> loanService.getLoansByUser("no-existe"));
    }

    // --- getLoansByBook ---

    @Test
    void getLoansByBook_deberiaRetornarPrestamosPorLibro() {
        when(bookRepository.existsById("book-1")).thenReturn(true);
        when(loanRepository.findByBookId("book-1")).thenReturn(List.of(loan));
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        List<Loan> result = loanService.getLoansByBook("book-1");

        assertEquals(1, result.size());
    }

    @Test
    void getLoansByBook_deberiaLanzarExcepcionSiLibroNoExiste() {
        when(bookRepository.existsById("no-existe")).thenReturn(false);

        assertThrows(BookNotFoundException.class,
                () -> loanService.getLoansByBook("no-existe"));
    }

}