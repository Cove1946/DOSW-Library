package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.core.model.User;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoanService {

    private final List<Loan> loans = new ArrayList<>();
    private final BookService bookService;
    private final UserService userService;

    public LoanService(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    public Loan createLoan(String bookId, String userId) throws BookNotAvailableException, UserNotFoundException {
        Book book = bookService.getBookById(bookId);
        User user = userService.getUserById(userId);

        int copies = bookService.getCopies(bookId);
        if (copies <= 0) {
            throw new BookNotAvailableException("No hay copias disponibles para: " + bookId);
        }

        bookService.updateCopies(bookId, copies - 1);

        Loan loan = new Loan(book, user, LocalDate.now(), Status.ACTIVE, null);
        loans.add(loan);
        return loan;
    }

    public Loan returnBook(String bookId, String userId) {
        Loan loan = loans.stream()
                .filter(l -> l.getBook().getId().equals(bookId))
                .filter(l -> l.getUser().getId().equals(userId))
                .filter(l -> l.getStatus() == Status.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Préstamo activo no encontrado"));

        loan.setStatus(Status.RETURNED);
        loan.setReturnDate(LocalDate.now());

        int copies = bookService.getCopies(bookId);
        bookService.updateCopies(bookId, copies + 1);

        return loan;
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

}

