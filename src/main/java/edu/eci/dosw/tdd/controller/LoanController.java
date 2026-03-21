package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestParam String bookId, @RequestParam String userId) {
        return ResponseEntity.status(201).body(loanService.createLoan(bookId, userId));
    }

    @PutMapping("/return")
    public ResponseEntity<Loan> returnBook(@RequestParam String bookId,
                                           @RequestParam String userId) {
        return ResponseEntity.ok(loanService.returnBook(bookId, userId));
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> getLoansByUser(@PathVariable String userId) {
        return ResponseEntity.ok(loanService.getLoansByUser(userId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Loan>> getLoanByBook (@PathVariable String bookId){
        return ResponseEntity.ok(loanService.getLoansByBook(bookId));
    }

    @PutMapping("/expire")
    public ResponseEntity<Loan> expireLoan(@RequestParam String bookId, @RequestParam String userId){
        return ResponseEntity.ok(loanService.expireLoan(bookId, userId));
    }
}