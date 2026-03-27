package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanPersistenceMapper {

    private final BookPersistenceMapper bookMapper;
    private final UserPersistenceMapper userMapper;

    public LoanPersistenceMapper(BookPersistenceMapper bookMapper, UserPersistenceMapper userMapper) {
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }

    public Loan toModel(LoanEntity entity) {
        if (entity == null) return null;
        return new Loan(
                bookMapper.toModel(entity.getBook()),
                userMapper.toModel(entity.getUser()),
                entity.getLoanDate(),
                Status.valueOf(entity.getStatus()),
                entity.getReturnDate()
        );
    }
}