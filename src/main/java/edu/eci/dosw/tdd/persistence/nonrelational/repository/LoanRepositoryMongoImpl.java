package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.LoanRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.LoanDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("mongo")
public class LoanRepositoryMongoImpl implements LoanRepository {

    private final MongoLoanRepository repository;
    private final LoanDocumentMapper mapper;

    public LoanRepositoryMongoImpl(MongoLoanRepository repository, LoanDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Loan save(Loan loan) {
        String userId = loan.getUser().getId();
        LoanDocument loanDoc = mapper.toDocument(loan);

        if (loanDoc.getId() == null) {
            loanDoc.setId(UUID.randomUUID().toString());
        }

        UserDocument userDoc = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        List<LoanDocument> loans = userDoc.getLoans();
        if (loans == null) loans = new ArrayList<>();

        boolean exists = loans.stream().anyMatch(l -> loanDoc.getId().equals(l.getId()));
        if (exists) {
            loans.replaceAll(l -> loanDoc.getId().equals(l.getId()) ? loanDoc : l);
        } else {
            loans.add(loanDoc);
        }

        userDoc.setLoans(loans);
        repository.save(userDoc);

        return mapper.toModel(loanDoc, userId);
    }

    @Override
    public Optional<Loan> findById(String id) {
        return repository.findByLoanId(id)
                .flatMap(userDoc -> userDoc.getLoans().stream()
                        .filter(l -> id.equals(l.getId()))
                        .findFirst()
                        .map(l -> mapper.toModel(l, userDoc.getId())));
    }

    @Override
    public List<Loan> findAll() {
        List<Loan> result = new ArrayList<>();
        for (UserDocument userDoc : repository.findAll()) {
            if (userDoc.getLoans() != null) {
                userDoc.getLoans().forEach(l -> result.add(mapper.toModel(l, userDoc.getId())));
            }
        }
        return result;
    }

    @Override
    public void delete(String id) {
        repository.findByLoanId(id).ifPresent(userDoc -> {
            userDoc.getLoans().removeIf(l -> id.equals(l.getId()));
            repository.save(userDoc);
        });
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return repository.findById(userId)
                .map(userDoc -> {
                    if (userDoc.getLoans() == null) return new ArrayList<Loan>();
                    return userDoc.getLoans().stream()
                            .map(l -> mapper.toModel(l, userId))
                            .toList();
                })
                .orElse(new ArrayList<>());
    }

    @Override
    public List<Loan> findByBookId(String bookId) {
        List<Loan> result = new ArrayList<>();
        for (UserDocument userDoc : repository.findByLoanBookId(bookId)) {
            userDoc.getLoans().stream()
                    .filter(l -> bookId.equals(l.getBookId()))
                    .forEach(l -> result.add(mapper.toModel(l, userDoc.getId())));
        }
        return result;
    }

    @Override
    public Optional<Loan> findByBookIdAndUserIdAndStatus(String bookId, String userId, String status) {
        return repository.findById(userId)
                .flatMap(userDoc -> userDoc.getLoans().stream()
                        .filter(l -> bookId.equals(l.getBookId()) && status.equals(l.getStatus()))
                        .findFirst()
                        .map(l -> mapper.toModel(l, userId)));
    }
}