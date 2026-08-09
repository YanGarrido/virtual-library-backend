package com.yan.virtuallibrary.Users.domain.entities;

import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Books.domain.entities.ReviewEntity;
import com.yan.virtuallibrary.Users.domain.enums.ReadFormat;
import com.yan.virtuallibrary.Users.domain.enums.ReadStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "tb_user_books",
        uniqueConstraints = {@UniqueConstraint(name = "uk_user_book", columnNames = {"user_id", "book_id"})}
)
public class UserBookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "started_at")
    private LocalDate startedAt;

    @Column(name = "finished_at")
    private LocalDate finishedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_status", nullable = false)
    private ReadStatus readStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_format", nullable = false)
    private ReadFormat readFormat;

    @OneToOne(mappedBy = "userBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private ReviewEntity reviewEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

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
