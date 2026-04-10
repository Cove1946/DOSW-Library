package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaLoanRepository extends JpaRepository<LoanEntity, Long> {
    List<LoanEntity> findByUserId(String userId);
    List<LoanEntity> findByBookId(String bookId);
    Optional<LoanEntity> findByBookIdAndUserIdAndStatus(String bookId, String userId, String status);
}