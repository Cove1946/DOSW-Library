package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.LoanRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.LoanDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        LoanDocument loanDoc = mapper.toDocument(loan);
        String userId = loan.getUser().getId();

        if (loanDoc.getId() == null) {
            loanDoc.setId(UUID.randomUUID().toString());
        }

        Query queryExists = new Query(
                Criteria.where("_id").is(userId)
                        .and("loans.id").is(loanDoc.getId())
        );
        boolean exists = mongoTemplate.exists(queryExists, UserDocument.class);

        if (exists) {
            Query q = new Query(Criteria.where("_id").is(userId).and("loans.id").is(loanDoc.getId()));
            Update u = new Update().set("loans.$", loanDoc);
            mongoTemplate.updateFirst(q, u, UserDocument.class);
        } else {
            Query q = new Query(Criteria.where("_id").is(userId));
            Update u = new Update().push("loans", loanDoc);
            mongoTemplate.updateFirst(q, u, UserDocument.class);
        }

        return mapper.toModel(loanDoc, userId);
    }

    @Override
    public Optional<Loan> findById(String id) {
        Query query = new Query(Criteria.where("loans.id").is(id));
        UserDocument userDoc = mongoTemplate.findOne(query, UserDocument.class);
        if (userDoc == null) return Optional.empty();

        return userDoc.getLoans().stream()
                .filter(l -> id.equals(l.getId()))
                .findFirst()
                .map(l -> mapper.toModel(l, userDoc.getId()));
    }

    @Override
    public List<Loan> findAll() {
        List<Loan> result = new ArrayList<>();
        for (UserDocument userDoc : mongoTemplate.findAll(UserDocument.class)) {
            if (userDoc.getLoans() != null) {
                userDoc.getLoans().forEach(l -> result.add(mapper.toModel(l, userDoc.getId())));
            }
        }
        return result;
    }

    @Override
    public void delete(String id) {
        Query query = new Query(Criteria.where("loans.id").is(id));
        Update update = new Update().pull("loans", new org.bson.Document("id", id));
        mongoTemplate.updateFirst(query, update, UserDocument.class);
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        UserDocument userDoc = mongoTemplate.findById(userId, UserDocument.class);
        if (userDoc == null || userDoc.getLoans() == null) return new ArrayList<>();
        return userDoc.getLoans().stream()
                .map(l -> mapper.toModel(l, userId))
                .toList();
    }

    @Override
    public List<Loan> findByBookId(String bookId) {
        List<Loan> result = new ArrayList<>();
        Query query = new Query(Criteria.where("loans.bookId").is(bookId));
        for (UserDocument userDoc : mongoTemplate.find(query, UserDocument.class)) {
            userDoc.getLoans().stream()
                    .filter(l -> bookId.equals(l.getBookId()))
                    .forEach(l -> result.add(mapper.toModel(l, userDoc.getId())));
        }
        return result;
    }

    @Override
    public Optional<Loan> findByBookIdAndUserIdAndStatus(String bookId, String userId, String status) {
        UserDocument userDoc = mongoTemplate.findById(userId, UserDocument.class);
        if (userDoc == null || userDoc.getLoans() == null) return Optional.empty();

        return userDoc.getLoans().stream()
                .filter(l -> bookId.equals(l.getBookId()) && status.equals(l.getStatus()))
                .findFirst()
                .map(l -> mapper.toModel(l, userId));
    }
}