package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    private static final int MAX_ACTIVE_LOANS = 3;
    private final List<Loan> loans = new ArrayList<>();
    private final BookService bookService;
    private final UserService userService;
    private final LoanValidator loanValidator;

    public LoanService(BookService bookService, UserService userService, LoanValidator loanValidator) {
        this.bookService = bookService;
        this.userService = userService;
        this.loanValidator = loanValidator;
    }

    public Loan createLoan(String bookId, String userId) {
        loanValidator.validateLoanRequest(bookId, userId);
        Book book = bookService.getBookById(bookId);
        User user = userService.getUserById(userId);

        if (!book.isAvailable() || bookService.getCopies(bookId) == 0) {
            throw new BookNotAvailableException(bookId);
        }

        long activeLoans = loans.stream()
                .filter(l -> l.getUser().getId().equals(userId) && l.getStatus() == Status.ACTIVE)
                .count();

        if (activeLoans >= MAX_ACTIVE_LOANS) {
            throw new LoanLimitExceededException(userId);
        }

        bookService.decrementCopy(bookId);

        Loan loan = new Loan(book, user, LocalDate.now(), Status.ACTIVE, null);
        loans.add(loan);
        return loan;
    }

    public Loan returnBook(String bookId, String userId) {
        Loan loan = loans.stream()
                .filter(l -> l.getBook().getId().equals(bookId)
                        && l.getUser().getId().equals(userId)
                        && l.getStatus() == Status.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró préstamo activo para libro " + bookId + " y usuario " + userId));

        loan.setStatus(Status.RETURNED);
        loan.setReturnDate(LocalDate.now());
        bookService.incrementCopy(bookId);
        return loan;
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public List<Loan> getLoansByUser(String userId) {
        userService.getUserById(userId); // valida que exista
        return loans.stream()
                .filter(l -> l.getUser().getId().equals(userId))
                .toList();
    }
}

