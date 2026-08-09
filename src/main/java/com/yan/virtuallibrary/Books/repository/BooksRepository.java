package com.yan.virtuallibrary.Books.repository;


import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenreContainingIgnoreCaseAndIsbnContainingIgnoreCase(String s, String s1, String s2, String s3);
}
