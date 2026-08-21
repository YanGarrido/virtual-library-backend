package com.yan.virtuallibrary.Books.repository;


import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Books.domain.enums.BookSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenreContainingIgnoreCaseAndIsbnContainingIgnoreCase(String s, String s1, String s2, String s3);

    Optional<BookEntity> findByExternalIdAndSource(String externalId, BookSource source);
}
