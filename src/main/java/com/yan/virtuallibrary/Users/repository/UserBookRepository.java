package com.yan.virtuallibrary.Users.repository;

import com.yan.virtuallibrary.Users.domain.entities.UserBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRepository extends JpaRepository<UserBookEntity, Long> {
}
