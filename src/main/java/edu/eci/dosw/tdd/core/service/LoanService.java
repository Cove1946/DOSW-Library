package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.relational.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.relational.mapper.LoanPersistenceMapper;
import edu.eci.dosw.tdd.persistence.relational.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.relational.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.relational.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanPersistenceMapper loanMapper;
    private final LoanValidator loanValidator;

    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository,
                       LoanPersistenceMapper loanMapper,
                       LoanValidator loanValidator) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanMapper = loanMapper;
        this.loanValidator = loanValidator;
    }

    public Loan createLoan(String bookId, String userId) {
        loanValidator.validateIds(bookId, userId);

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado con ID: " + bookId));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        loanValidator.validateBookAvailable(book.getAvailableCopies());

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        LoanEntity loan = new LoanEntity(null, user, book, DateUtil.today(), null, Status.ACTIVE.name(), null);
        return loanMapper.toModel(loanRepository.save(loan));
    }

    public Loan returnBook(String bookId, String userId) {
        loanValidator.validateIds(bookId, userId);

        LoanEntity loanEntity = loanRepository
                .findByBookIdAndUserIdAndStatus(bookId, userId, Status.ACTIVE.name())
                .orElseThrow(() -> new RuntimeException("Préstamo activo no encontrado para el libro y usuario indicados"));

        loanValidator.validateActiveLoan(loanMapper.toModel(loanEntity));

        loanEntity.setStatus(Status.RETURNED.name());
        loanEntity.setReturnDate(DateUtil.today());

        BookEntity book = loanEntity.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loanMapper.toModel(loanRepository.save(loanEntity));
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(loanMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<Loan> getLoansByUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + userId);
        }
        return loanRepository.findByUserId(userId)
                .stream()
                .map(loanMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<Loan> getLoansByBook(String bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Libro no encontrado con ID: " + bookId);
        }
        return loanRepository.findByBookId(bookId)
                .stream()
                .map(loanMapper::toModel)
                .collect(Collectors.toList());
    }

    public Loan expireLoan(String bookId, String userId) {
        loanValidator.validateIds(bookId, userId);

        LoanEntity loanEntity = loanRepository
                .findByBookIdAndUserIdAndStatus(bookId, userId, Status.ACTIVE.name())
                .orElseThrow(() -> new RuntimeException("Préstamo activo no encontrado"));

        loanEntity.setStatus(Status.EXPIRED.name());
        return loanMapper.toModel(loanRepository.save(loanEntity));
    }
}