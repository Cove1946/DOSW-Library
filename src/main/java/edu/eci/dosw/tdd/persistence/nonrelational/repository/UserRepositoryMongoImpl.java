package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.UserRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.UserDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class UserRepositoryMongoImpl implements UserRepository {

    private final MongoTemplate mongoTemplate;
    private final UserDocumentMapper mapper;

    public UserRepositoryMongoImpl(MongoTemplate mongoTemplate, UserDocumentMapper mapper) {
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserDocument doc = mapper.toDocument(user);
        return mapper.toModel(mongoTemplate.save(doc));
    }

    @Override
    public Optional<User> findById(String id) {
        UserDocument doc = mongoTemplate.findById(id, UserDocument.class);
        return Optional.ofNullable(doc).map(mapper::toModel);
    }

    @Override
    public List<User> findAll() {
        return mongoTemplate.findAll(UserDocument.class)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void delete(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, UserDocument.class);
    }

    @Override
    public boolean existsById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.exists(query, UserDocument.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        UserDocument doc = mongoTemplate.findOne(query, UserDocument.class);
        return Optional.ofNullable(doc).map(mapper::toModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return mongoTemplate.exists(query, UserDocument.class);
    }

    @Override
    public List<User> findByRole(String role) {
        Query query = new Query(Criteria.where("role").is(role));
        return mongoTemplate.find(query, UserDocument.class)
                .stream()
                .map(mapper::toModel)
                .toList();
    }
}