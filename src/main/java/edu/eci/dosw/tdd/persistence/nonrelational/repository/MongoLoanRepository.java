package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MongoLoanRepository extends MongoRepository<LoanDocument, String> {
    List<LoanDocument> findByUserId(String userId);
    List<LoanDocument> findByBookId(String bookId);
    Optional<LoanDocument> findByBookIdAndUserIdAndStatus(String bookId, String userId, String status);
}