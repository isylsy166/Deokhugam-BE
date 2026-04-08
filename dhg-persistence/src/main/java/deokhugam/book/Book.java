package deokhugam.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "books",
    uniqueConstraints = {@UniqueConstraint(name = "uq_books_isbn", columnNames = "isbn")}
)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isbn;

    private String title;

    private String author;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String thumbnail_url;

    private String publisher;

    private LocalDate published_at;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private LocalDateTime deleted_at;

}
