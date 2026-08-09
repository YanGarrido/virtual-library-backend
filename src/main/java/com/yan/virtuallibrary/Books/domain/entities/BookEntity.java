package com.yan.virtuallibrary.Books.domain.entities;

import com.yan.virtuallibrary.Books.domain.enums.BookSource;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tb_books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name = "publisher")
    private String publisher;
    @Column(name = "isbn")
    private String isbn;
    @Column(name = "synopsis", columnDefinition = "TEXT")
    private String synopsis;
    @Column (name = "genre")
    private String genre;
    @Column(name = "cover_url")
    private String coverUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private BookSource source = BookSource.MANUAL;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updated_at;

    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }
}
