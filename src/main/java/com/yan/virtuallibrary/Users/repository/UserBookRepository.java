package com.yan.virtuallibrary.Users.repository;

import com.yan.virtuallibrary.Users.domain.entities.UserBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBookEntity, Long> {
    List<UserBookEntity> findAllByUser_Id(Long id);
    Optional<UserBookEntity> findByUser_IdAndBook_Id(Long userId, Long bookId);
    boolean existsByUser_IdAndBook_Id(Long userId, Long bookId);
}
