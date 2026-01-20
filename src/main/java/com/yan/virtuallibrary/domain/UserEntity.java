package com.yan.virtuallibrary.domain;

import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotBlank(message = "Nome é obrigatório")
    @Column(name = "name", nullable = false)
    private String name;

    //@NotBlank(message = "Username é obrigatório")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    //@Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    //@NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = true)
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
