package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {

    List<BookEntity> findByAvailableCopiesGreaterThan(int copies);
}