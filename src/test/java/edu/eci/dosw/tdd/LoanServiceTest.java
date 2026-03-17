package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.BookService;
import edu.eci.dosw.tdd.core.service.LoanService;
import edu.eci.dosw.tdd.core.service.UserService;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanServiceTest {

    private BookService bookService;
    private UserService userService;
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(new BookValidator());
        userService = new UserService(new UserValidator());
        loanService = new LoanService(bookService, userService, new LoanValidator());
    }

    // Escenario exitoso: crear préstamo
    @Test
    void shouldCreateLoanSuccessfully() {
        Loan loan = loanService.createLoan("B001", "U001");
        assertNotNull(loan);
        assertEquals(Status.ACTIVE, loan.getStatus());
        assertEquals("B001", loan.getBook().getId());
    }

    // Escenario exitoso: devolver libro
    @Test
    void shouldReturnBookSuccessfully() {
        loanService.createLoan("B001", "U001");
        Loan returned = loanService.returnBook("B001", "U001");
        assertEquals(Status.RETURNED, returned.getStatus());
        assertNotNull(returned.getReturnDate());
    }

    // Error: libro no disponible
    @Test
    void shouldThrowWhenBookNotAvailable() {
        // Agota el ejemplar único
        bookService.addBook(new Book("B005", "Agotado", "Autor", false), 0);
        assertThrows(BookNotAvailableException.class,
                () -> loanService.createLoan("B005", "U001"));
    }

    // Error: limite de préstamos activos (3)
    @Test
    void shouldThrowWhenLoanLimitExceeded() {
        loanService.createLoan("B001", "U001");
        loanService.createLoan("B002", "U001");
        loanService.createLoan("B003", "U001");
        assertThrows(LoanLimitExceededException.class,
                () -> loanService.createLoan("B004", "U001"));
    }

    // Error: intentar devolver un préstamo inexistente
    @Test
    void shouldThrowWhenReturningNonExistentLoan() {
        assertThrows(IllegalArgumentException.class,
                () -> loanService.returnBook("B001", "U001"));
    }

    // Escenario: obtener préstamos por usuario
    @Test
    void shouldGetLoansByUser() {
        loanService.createLoan("B001", "U001");
        loanService.createLoan("B002", "U001");
        assertEquals(2, loanService.getLoansByUser("U001").size());
    }

    // Escenario: al devolver libro, se incrementa el inventario
    @Test
    void shouldIncrementInventoryAfterReturn() {
        loanService.createLoan("B002", "U001"); // B002 tiene 1 copia
        assertEquals(0, bookService.getCopies("B002"));
        loanService.returnBook("B002", "U001");
        assertEquals(1, bookService.getCopies("B002"));
    }

    // Escenario: obtener todos los préstamos
    @Test
    void shouldGetAllLoans() {
        loanService.createLoan("B001", "U001");
        assertEquals(1, loanService.getAllLoans().size());
    }
}