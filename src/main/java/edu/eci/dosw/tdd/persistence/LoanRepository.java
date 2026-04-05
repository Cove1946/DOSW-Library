package edu.eci.dosw.tdd.persistence;

import edu.eci.dosw.tdd.core.model.Loan;
import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    Loan save(Loan loan);
    Optional<Loan> findById(String id);
    List<Loan> findAll();
    void delete(String id);
    List<Loan> findByUserId(String userId);
    List<Loan> findByBookId(String bookId);
    Optional<Loan> findByBookIdAndUserIdAndStatus(String bookId, String userId, String status);
}