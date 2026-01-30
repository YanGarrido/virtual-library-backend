package com.yan.virtuallibrary.Users.domain.entities;

import com.yan.virtuallibrary.Books.domain.entities.BooksEntity;
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
@Table(name = "tb_user_books")
public class UserBookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BooksEntity book;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_status", nullable = false)
    private ReadStatus readStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_format", nullable = false)
    private ReadFormat readFormat;

    @OneToOne(mappedBy = "userBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private ReviewEntity reviewEntity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
