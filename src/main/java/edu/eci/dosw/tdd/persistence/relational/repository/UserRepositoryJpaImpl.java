package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.UserRepository;
import edu.eci.dosw.tdd.persistence.relational.entity.UserEntity;
import edu.eci.dosw.tdd.persistence.relational.mapper.UserPersistenceMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class UserRepositoryJpaImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    private final UserPersistenceMapper mapper;

    public UserRepositoryJpaImpl(UserPersistenceMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }
        return mapper.toModel(entity);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(em.find(UserEntity.class, id)).map(mapper::toModel);
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM UserEntity u", UserEntity.class)
                .getResultList().stream().map(mapper::toModel).toList();
    }

    @Override
    public void delete(String id) {
        UserEntity entity = em.find(UserEntity.class, id);
        if (entity != null) em.remove(entity);
    }

    @Override
    public boolean existsById(String id) {
        return em.find(UserEntity.class, id) != null;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return em.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class)
                .setParameter("username", username)
                .getResultStream().findFirst().map(mapper::toModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    @Override
    public List<User> findByRole(String role) {
        return em.createQuery("SELECT u FROM UserEntity u WHERE u.role = :role", UserEntity.class)
                .setParameter("role", role)
                .getResultList().stream().map(mapper::toModel).toList();
    }
}