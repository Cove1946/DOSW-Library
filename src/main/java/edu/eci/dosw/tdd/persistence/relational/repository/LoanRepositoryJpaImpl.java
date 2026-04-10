package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.LoanRepository;
import edu.eci.dosw.tdd.persistence.relational.mapper.LoanPersistenceMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class LoanRepositoryJpaImpl implements LoanRepository {

    private final JpaLoanRepository repository;
    private final LoanPersistenceMapper mapper;

    public LoanRepositoryJpaImpl(JpaLoanRepository repository, LoanPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Loan save(Loan loan) {
        return mapper.toModel(repository.save(mapper.toEntity(loan)));
    }

    @Override
    public Optional<Loan> findById(String id) {
        return repository.findById(Long.valueOf(id)).map(mapper::toModel);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(Long.valueOf(id));
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return repository.findByUserId(userId).stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Loan> findByBookId(String bookId) {
        return repository.findByBookId(bookId).stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<Loan> findByBookIdAndUserIdAndStatus(String bookId, String userId, String status) {
        return repository.findByBookIdAndUserIdAndStatus(bookId, userId, status).map(mapper::toModel);
    }
}