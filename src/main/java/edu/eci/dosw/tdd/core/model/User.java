package edu.eci.dosw.tdd.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {
    private String id;
    private String name;
    private String username;
    private String password;
    private Role role;

    private String email;
    private MembershipType membershipType;
    private LocalDate addedDate;
}

