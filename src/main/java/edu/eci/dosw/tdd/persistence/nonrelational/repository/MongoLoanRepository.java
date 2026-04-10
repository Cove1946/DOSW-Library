package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MongoLoanRepository extends MongoRepository<UserDocument, String> {
    @Query("{ 'loans.id': ?0 }")
    Optional<UserDocument> findByLoanId(String loanId);

    @Query("{ 'loans.bookId': ?0 }")
    List<UserDocument> findByLoanBookId(String bookId);

    @Query("{ 'loans.bookId': ?0, 'loans.status': ?1 }")
    List<UserDocument> findByLoanBookIdAndStatus(String bookId, String status);
}