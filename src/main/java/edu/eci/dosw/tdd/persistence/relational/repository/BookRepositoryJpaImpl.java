package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.BookRepository;
import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.relational.mapper.BookPersistenceMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class BookRepositoryJpaImpl implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    private final BookPersistenceMapper mapper;

    public BookRepositoryJpaImpl(BookPersistenceMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Book save(Book book) {
        BookEntity entity = mapper.toEntity(book);
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }
        return mapper.toModel(entity);
    }

    @Override
    public Optional<Book> findById(String id) {
        BookEntity entity = em.find(BookEntity.class, id);
        return Optional.ofNullable(entity).map(mapper::toModel);
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM BookEntity b", BookEntity.class)
                .getResultList()
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void delete(String id) {
        BookEntity entity = em.find(BookEntity.class, id);
        if (entity != null) em.remove(entity);
    }

    @Override
    public boolean existsById(String id) {
        return em.find(BookEntity.class, id) != null;
    }
}