package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.*;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanHistoryDocument;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanDocumentMapper {

    public Loan toModel(LoanDocument doc, String userId) {
        if (doc == null) return null;

        List<LoanHistory> history = doc.getHistory() == null
                ? new ArrayList<>()
                : doc.getHistory().stream()
                .map(h -> new LoanHistory(
                        h.getStatus() != null ? Status.valueOf(h.getStatus()) : null,
                        h.getExecutionDate()
                ))
                .collect(Collectors.toList());

        Book book = new Book();
        book.setId(doc.getBookId());

        User user = new User();
        user.setId(userId);

        return new Loan(
                doc.getId(),
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
                ? new ArrayList<>()
                : model.getHistory().stream()
                .map(h -> new LoanHistoryDocument(
                        h.getStatus() != null ? h.getStatus().name() : null,
                        h.getExecutionDate()
                ))
                .collect(Collectors.toList());

        LoanDocument doc = new LoanDocument();
        doc.setId(model.getId());
        doc.setBookId(model.getBook() != null ? model.getBook().getId() : null);
        doc.setLoanDate(model.getLoanDate());
        doc.setReturnDate(model.getReturnDate());
        doc.setStatus(model.getStatus() != null ? model.getStatus().name() : null);
        doc.setHistory(historyDocs);
        return doc;
    }
}