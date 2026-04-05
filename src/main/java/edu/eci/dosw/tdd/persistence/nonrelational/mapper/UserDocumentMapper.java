package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.MembershipType;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import org.springframework.stereotype.Component;

@Component
public class UserDocumentMapper {

    public User toModel(UserDocument doc) {
        if (doc == null) return null;
        return new User(
                doc.getId(),
                doc.getName(),
                doc.getUsername(),
                doc.getPassword(),
                doc.getRole() != null ? Role.valueOf(doc.getRole()) : null,
                doc.getEmail(),
                doc.getMembershipType() != null ? MembershipType.valueOf(doc.getMembershipType()) : null,
                doc.getAddedDate()
        );
    }

    public UserDocument toDocument(User model) {
        if (model == null) return null;
        return new UserDocument(
                model.getId(),
                model.getName(),
                model.getUsername(),
                model.getPassword(),
                model.getRole() != null ? model.getRole().name() : null,
                model.getEmail(),
                model.getMembershipType() != null ? model.getMembershipType().name() : null,
                model.getAddedDate()
        );
    }
}