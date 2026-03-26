package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

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
        loanValidator.validateIds(bookId, userId);

        Book book = bookService.getBookById(bookId);
        User user = userService.getUserById(userId);

        loanValidator.validateBookAvailable(book.getAvailableCopies());

        bookService.decreaseAvailableCopies(bookId);

        Loan loan = new Loan(book, user, DateUtil.today(), Status.ACTIVE, null);
        loans.add(loan);
        return loan;
    }

    public Loan returnBook(String bookId, String userId) {
        loanValidator.validateIds(bookId, userId);

        Loan loan = loans.stream()
                .filter(l -> l.getBook().getId().equals(bookId))
                .filter(l -> l.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prestamo no encontrado para el libro y usuario indicados"));

        loanValidator.validateActiveLoan(loan);

        loan.setStatus(Status.RETURNED);
        loan.setReturnDate(DateUtil.today());

        bookService.increaseAvailableCopies(bookId);

        return loan;
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public List<Loan> getLoansByUser(String userId) {
        userService.getUserById(userId);
        return loans.stream()
                .filter(l -> l.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Loan> getLoansByBook(String bookId) {
        bookService.getBookById(bookId);
        return loans.stream()
                .filter(l -> l.getBook().getId().equals(bookId))
                .collect(Collectors.toList());
    }

    public Loan expireLoan(String bookId, String userId) {
        loanValidator.validateIds(bookId, userId);

        Loan loan = loans.stream()
                .filter(l -> l.getBook().getId().equals(bookId))
                .filter(l -> l.getUser().getId().equals(userId))
                .filter(l -> l.getStatus() == Status.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Préstamo activo no encontrado"));

        loan.setStatus(Status.EXPIRED);
        return loan;
    }
}
