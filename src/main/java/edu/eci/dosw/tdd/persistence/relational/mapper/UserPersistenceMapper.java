package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.MembershipType;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.relational.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public User toModel(UserEntity entity) {
        if (entity == null) return null;
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole() != null ? Role.valueOf(entity.getRole()) : null,
                entity.getEmail(),
                entity.getMembershipType() != null ? MembershipType.valueOf(entity.getMembershipType()) : null,
                entity.getAddedDate()
        );
    }

    public UserEntity toEntity(User model) {
        if (model == null) return null;
        return new UserEntity(
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