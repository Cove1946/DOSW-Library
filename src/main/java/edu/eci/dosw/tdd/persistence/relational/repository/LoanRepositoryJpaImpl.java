package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.LoanRepository;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.relational.mapper.LoanPersistenceMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
@Transactional
public class LoanRepositoryJpaImpl implements LoanRepository {

    @PersistenceContext
    private EntityManager em;

    private final LoanPersistenceMapper mapper;

    public LoanRepositoryJpaImpl(LoanPersistenceMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Loan save(Loan loan) {
        LoanEntity entity = mapper.toEntity(loan);
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }
        return mapper.toModel(entity);
    }

    @Override
    public Optional<Loan> findById(String id) {
        return Optional.ofNullable(em.find(LoanEntity.class, Long.valueOf(id))).map(mapper::toModel);
    }

    @Override
    public List<Loan> findAll() {
        return em.createQuery("SELECT l FROM LoanEntity l", LoanEntity.class)
                .getResultList().stream().map(mapper::toModel).toList();
    }

    @Override
    public void delete(String id) {
        LoanEntity entity = em.find(LoanEntity.class, Long.valueOf(id));
        if (entity != null) em.remove(entity);
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return em.createQuery("SELECT l FROM LoanEntity l WHERE l.user.id = :userId", LoanEntity.class)
                .setParameter("userId", userId)
                .getResultList().stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Loan> findByBookId(String bookId) {
        return em.createQuery("SELECT l FROM LoanEntity l WHERE l.book.id = :bookId", LoanEntity.class)
                .setParameter("bookId", bookId)
                .getResultList().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<Loan> findByBookIdAndUserIdAndStatus(String bookId, String userId, String status) {
        return em.createQuery(
                        "SELECT l FROM LoanEntity l WHERE l.book.id = :bookId AND l.user.id = :userId AND l.status = :status",
                        LoanEntity.class)
                .setParameter("bookId", bookId)
                .setParameter("userId", userId)
                .setParameter("status", status)
                .getResultStream().findFirst().map(mapper::toModel);
    }
}