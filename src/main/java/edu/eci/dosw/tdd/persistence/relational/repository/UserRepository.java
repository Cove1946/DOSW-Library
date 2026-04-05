package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.persistence.relational.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    List<UserEntity> findByRole(String role);

    boolean existsByUsername(String username);
}