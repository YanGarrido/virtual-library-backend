package com.yan.virtuallibrary.Books.repository;


import com.yan.virtuallibrary.Books.domain.entities.BooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<BooksEntity, Long> {
}
