package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.LoanRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.LoanDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class LoanRepositoryMongoImpl implements LoanRepository {

    private final MongoTemplate mongoTemplate;
    private final LoanDocumentMapper mapper;

    public LoanRepositoryMongoImpl(MongoTemplate mongoTemplate, LoanDocumentMapper mapper) {
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;
    }

    @Override
    public Loan save(Loan loan) {
        LoanDocument doc = mapper.toDocument(loan);
        return mapper.toModel(mongoTemplate.save(doc));
    }

    @Override
    public Optional<Loan> findById(String id) {
        LoanDocument doc = mongoTemplate.findById(id, LoanDocument.class);
        return Optional.ofNullable(doc).map(mapper::toModel);
    }

    @Override
    public List<Loan> findAll() {
        return mongoTemplate.findAll(LoanDocument.class)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void delete(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, LoanDocument.class);
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, LoanDocument.class)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public List<Loan> findByBookId(String bookId) {
        Query query = new Query(Criteria.where("bookId").is(bookId));
        return mongoTemplate.find(query, LoanDocument.class)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public Optional<Loan> findByBookIdAndUserIdAndStatus(String bookId, String userId, String status) {
        Query query = new Query(Criteria.where("bookId").is(bookId)
                .and("userId").is(userId)
                .and("status").is(status));
        LoanDocument doc = mongoTemplate.findOne(query, LoanDocument.class);
        return Optional.ofNullable(doc).map(mapper::toModel);
    }
}