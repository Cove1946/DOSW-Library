package edu.eci.dosw.tdd;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanServiceTest {

    private LoanService loanService;
    private BookService bookService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(new BookValidator());
        userService = new UserService(new UserValidator());
        loanService = new LoanService(bookService, userService, new LoanValidator());
        bookService.addBook(new Book("B1", "Clean Code", "Robert Martin", true), 3);
        userService.registerUser(new User("U1", "Lees"));
    }

    // ─── EXITOSOS ─────────────────────────────────────────

    @Test
    void createLoan_shouldCreateLoanSuccessfully() {
        Loan loan = loanService.createLoan("B1", "U1");
        assertNotNull(loan);
        assertEquals(Status.ACTIVE, loan.getStatus());
        assertEquals(2, bookService.getCopies("B1"));
    }

    @Test
    void returnBook_shouldReturnLoanSuccessfully() {
        loanService.createLoan("B1", "U1");
        Loan loan = loanService.returnBook("B1", "U1");
        assertEquals(Status.RETURNED, loan.getStatus());
        assertNotNull(loan.getReturnDate());
        assertEquals(3, bookService.getCopies("B1"));
    }

    @Test
    void expireLoan_shouldExpireLoanSuccessfully() {
        loanService.createLoan("B1", "U1");
        Loan loan = loanService.expireLoan("B1", "U1");
        assertEquals(Status.EXPIRED, loan.getStatus());
    }

    @Test
    void getAllLoans_shouldReturnAllLoans() {
        loanService.createLoan("B1", "U1");
        List<Loan> loans = loanService.getAllLoans();
        assertEquals(1, loans.size());
    }

    @Test
    void getLoansByUser_shouldReturnLoansForUser() {
        loanService.createLoan("B1", "U1");
        List<Loan> loans = loanService.getLoansByUser("U1");
        assertEquals(1, loans.size());
    }

    @Test
    void getLoansByBook_shouldReturnLoansForBook() {
        loanService.createLoan("B1", "U1");
        List<Loan> loans = loanService.getLoansByBook("B1");
        assertEquals(1, loans.size());
    }

    // ─── ERROR ────────────────────────────────────────────

    @Test
    void createLoan_shouldThrowException_whenNoCopiesAvailable() {
        bookService.addBook(new Book("B2", "Libro Agotado", "Autor", true), 1);
        userService.registerUser(new User("U2", "Otro Usuario"));
        loanService.createLoan("B2", "U2");

        assertThrows(BookNotAvailableException.class, () ->
                loanService.createLoan("B2", "U2")
        );
    }

    @Test
    void createLoan_shouldThrowException_whenBookIdIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                loanService.createLoan("", "U1")
        );
    }

    @Test
    void createLoan_shouldThrowException_whenUserIdIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                loanService.createLoan("B1", "")
        );
    }

    @Test
    void returnBook_shouldThrowException_whenNoActiveLoanFound() {
        assertThrows(RuntimeException.class, () ->
                loanService.returnBook("B1", "U1")
        );
    }
}