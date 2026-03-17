package edu.eci.dosw.tdd.core.model;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Book {
    @EqualsAndHashCode.Include
    private String id;
    private String title;
    private String author;
    private boolean available;
}

