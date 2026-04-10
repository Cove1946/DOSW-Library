package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.BookRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.BookDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class BookRepositoryMongoImpl implements BookRepository {

    private final MongoBookRepository repository;
    private final BookDocumentMapper mapper;

    public BookRepositoryMongoImpl(MongoBookRepository repository, BookDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Book save(Book book) {
        return mapper.toModel(repository.save(mapper.toDocument(book)));
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