package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.BookRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.BookDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class BookRepositoryMongoImpl implements BookRepository {

    private final MongoTemplate mongoTemplate;
    private final BookDocumentMapper mapper;

    public BookRepositoryMongoImpl(MongoTemplate mongoTemplate, BookDocumentMapper mapper) {
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;
    }

    @Override
    public Book save(Book book) {
        BookDocument doc = mapper.toDocument(book);
        return mapper.toModel(mongoTemplate.save(doc));
    }

    @Override
    public Optional<Book> findById(String id) {
        BookDocument doc = mongoTemplate.findById(id, BookDocument.class);
        return Optional.ofNullable(doc).map(mapper::toModel);
    }

    @Override
    public List<Book> findAll() {
        return mongoTemplate.findAll(BookDocument.class)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void delete(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, BookDocument.class);
    }

    @Override
    public boolean existsById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.exists(query, BookDocument.class);
    }
}