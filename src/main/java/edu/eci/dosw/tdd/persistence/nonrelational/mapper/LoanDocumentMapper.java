package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.*;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanHistoryDocument;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class LoanDocumentMapper {

    public Loan toModel(LoanDocument doc) {
        if (doc == null) return null;

        List<LoanHistory> history = doc.getHistory() == null
                ? Collections.emptyList()
                : doc.getHistory().stream()
                .map(h -> new LoanHistory(
                        h.getStatus() != null ? Status.valueOf(h.getStatus()) : null,
                        h.getExecutionDate()
                ))
                .toList();

        Book book = new Book();
        book.setId(doc.getBookId());

        User user = new User();
        user.setId(doc.getUserId());

        return new Loan(
                book,
                user,
                doc.getLoanDate(),
                doc.getStatus() != null ? Status.valueOf(doc.getStatus()) : null,
                doc.getReturnDate(),
                history
        );
    }

    public LoanDocument toDocument(Loan model) {
        if (model == null) return null;

        List<LoanHistoryDocument> historyDocs = model.getHistory() == null
                ? Collections.emptyList()
                : model.getHistory().stream()
                .map(h -> new LoanHistoryDocument(
                        h.getStatus() != null ? h.getStatus().name() : null,
                        h.getExecutionDate()
                ))
                .toList();

        return new LoanDocument(
                null,
                model.getBook().getId(),
                model.getUser().getId(),
                model.getLoanDate(),
                model.getReturnDate(),
                model.getStatus() != null ? model.getStatus().name() : null,
                historyDocs
        );
    }
}