package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.BookRepository;
import edu.eci.dosw.tdd.persistence.relational.mapper.BookPersistenceMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class BookRepositoryJpaImpl implements BookRepository {

    private final JpaBookRepository repository;
    private final BookPersistenceMapper mapper;

    public BookRepositoryJpaImpl(JpaBookRepository repository, BookPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Book save(Book book) {
        return mapper.toModel(repository.save(mapper.toEntity(book)));
    }

    @Override
    public Optional<Book> findById(String id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }
}