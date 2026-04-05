package edu.eci.dosw.tdd.persistence.nonrelational.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;
    private String name;
    private String username;
    private String password;
    private String role;
    private String email;
    private String membershipType;
    private LocalDate addedDate;
}