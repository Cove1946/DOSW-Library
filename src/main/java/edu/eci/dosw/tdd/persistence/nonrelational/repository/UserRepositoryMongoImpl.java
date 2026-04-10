package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.UserRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.UserDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class UserRepositoryMongoImpl implements UserRepository {

    private final MongoUserRepository repository;
    private final UserDocumentMapper mapper;

    public UserRepositoryMongoImpl(MongoUserRepository repository, UserDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        return mapper.toModel(repository.save(mapper.toDocument(user)));
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<User> findAll() {
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

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public List<User> findByRole(String role) {
        return repository.findByRole(role).stream().map(mapper::toModel).toList();
    }
}