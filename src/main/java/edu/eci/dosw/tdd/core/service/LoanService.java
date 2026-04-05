package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.LoanHistory;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.persistence.BookRepository;
import edu.eci.dosw.tdd.persistence.LoanRepository;
import edu.eci.dosw.tdd.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        List<LoanHistory> history = new ArrayList<>();
        history.add(new LoanHistory(Status.ACTIVE, DateUtil.today()));

        Loan loan = new Loan(null, book, user, DateUtil.today(), Status.ACTIVE, null, history);
        return enrichLoan(loanRepository.save(loan));
    }

    public Loan returnBook(String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + loanId));

        loanValidator.validateActiveLoan(loan);

        if (loan.getHistory() == null) {
            loan.setHistory(new ArrayList<>());
        }
        loan.getHistory().add(new LoanHistory(Status.RETURNED, DateUtil.today()));
        loan.setStatus(Status.RETURNED);
        loan.setReturnDate(DateUtil.today());

        Book book = bookRepository.findById(loan.getBook().getId())
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado"));
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return enrichLoan(loanRepository.save(loan));
    }

    public Loan expireLoan(String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + loanId));

        if (loan.getHistory() == null) {
            loan.setHistory(new ArrayList<>());
        }
        loan.getHistory().add(new LoanHistory(Status.EXPIRED, DateUtil.today()));
        loan.setStatus(Status.EXPIRED);
        return enrichLoan(loanRepository.save(loan));
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::enrichLoan)
                .toList();
    }

    public List<Loan> getLoansByUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
        return loanRepository.findByUserId(userId).stream()
                .map(this::enrichLoan)
                .toList();
    }

    public List<Loan> getLoansByBook(String bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Libro no encontrado con ID: " + bookId);
        }
        return loanRepository.findByBookId(bookId).stream()
                .map(this::enrichLoan)
                .toList();
    }

    private Loan enrichLoan(Loan loan) {
        Book book = bookRepository.findById(loan.getBook().getId()).orElse(loan.getBook());
        User user = userRepository.findById(loan.getUser().getId()).orElse(loan.getUser());
        loan.setBook(book);
        loan.setUser(user);
        return loan;
    }
}