package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.persistence.BookRepository;
import edu.eci.dosw.tdd.persistence.LoanRepository;
import edu.eci.dosw.tdd.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanValidator loanValidator;

    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository,
                       LoanValidator loanValidator) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanValidator = loanValidator;
    }

    public Loan createLoan(String bookId, String userId) {
        loanValidator.validateIds(bookId, userId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado con ID: " + bookId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        loanValidator.validateBookAvailable(book.getAvailableCopies());

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Loan loan = new Loan(book, user, DateUtil.today(), Status.ACTIVE, null, Collections.emptyList());
        return loanRepository.save(loan);
    }

    public Loan returnBook(String bookId, String userId) {
        loanValidator.validateIds(bookId, userId);

        Loan loan = loanRepository
                .findByBookIdAndUserIdAndStatus(bookId, userId, Status.ACTIVE.name())
                .orElseThrow(() -> new RuntimeException("Préstamo activo no encontrado para el libro y usuario indicados"));

        loanValidator.validateActiveLoan(loan);

        loan.setStatus(Status.RETURNED);
        loan.setReturnDate(DateUtil.today());

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado con ID: " + bookId));
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getLoansByUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
        return loanRepository.findByUserId(userId);
    }

    public List<Loan> getLoansByBook(String bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Libro no encontrado con ID: " + bookId);
        }
        return loanRepository.findByBookId(bookId);
    }

    public Loan expireLoan(String bookId, String userId) {
        loanValidator.validateIds(bookId, userId);

        Loan loan = loanRepository
                .findByBookIdAndUserIdAndStatus(bookId, userId, Status.ACTIVE.name())
                .orElseThrow(() -> new RuntimeException("Préstamo activo no encontrado"));

        loan.setStatus(Status.EXPIRED);
        return loanRepository.save(loan);
    }
}