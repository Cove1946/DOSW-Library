package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.MembershipType;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toModel(UserDTO dto) {
        Role role = null;
        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            role = Role.valueOf(dto.getRole().toUpperCase());
        }

        MembershipType membership = null;
        if (dto.getMembershipType() != null && !dto.getMembershipType().isBlank()) {
            membership = MembershipType.valueOf(dto.getMembershipType().toUpperCase());
        }

        return new User(
                dto.getId(),
                dto.getName(),
                dto.getUsername(),
                dto.getPassword(),
                role,
                dto.getEmail(),
                membership,
                dto.getAddedDate()
        );
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                null,  // nunca exponer el password
                user.getRole() != null ? user.getRole().name() : null,
                user.getEmail(),
                user.getMembershipType() != null ? user.getMembershipType().name() : null,
                user.getAddedDate()
        );
    }
}