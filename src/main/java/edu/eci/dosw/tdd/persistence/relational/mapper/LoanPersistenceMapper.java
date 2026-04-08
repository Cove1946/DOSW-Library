package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.LoanHistory;
import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanHistoryEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

        List<LoanHistory> history = entity.getHistory() == null
                ? Collections.emptyList()
                : entity.getHistory().stream()
                .map(h -> new LoanHistory(
                        h.getStatusName() != null ? Status.valueOf(h.getStatusName()) : null,
                        h.getExecutionDate()
                ))
                .collect(Collectors.toList());

        return new Loan(
                String.valueOf(entity.getId()),
                bookMapper.toModel(entity.getBook()),
                userMapper.toModel(entity.getUser()),
                entity.getLoanDate(),
                Status.valueOf(entity.getStatus()),
                entity.getReturnDate(),
                history
        );
    }

    public LoanEntity toEntity(Loan model) {
        if (model == null) return null;

        LoanEntity entity = new LoanEntity();
        if (model.getId() != null && !model.getId().isBlank()) {
            entity.setId(Long.valueOf(model.getId()));
        }
        entity.setBook(bookMapper.toEntity(model.getBook()));
        entity.setUser(userMapper.toEntity(model.getUser()));
        entity.setLoanDate(model.getLoanDate());
        entity.setStatus(model.getStatus() != null ? model.getStatus().name() : null);
        entity.setReturnDate(model.getReturnDate());

        List<LoanHistoryEntity> historyEntities = model.getHistory() == null
                ? new ArrayList<>()
                : model.getHistory().stream()
                .map(h -> {
                    LoanHistoryEntity lhe = new LoanHistoryEntity();
                    lhe.setStatusName(h.getStatus() != null ? h.getStatus().name() : null);
                    lhe.setExecutionDate(h.getExecutionDate());
                    lhe.setLoan(entity);
                    return lhe;
                })
                .collect(Collectors.toList());

        entity.setHistory(historyEntities);
        return entity;
    }
}