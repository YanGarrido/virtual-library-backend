package com.yan.virtuallibrary.Books.service;

import com.yan.virtuallibrary.Books.domain.entities.BooksEntity;
import com.yan.virtuallibrary.Books.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksService {

    @Autowired
    private BooksRepository booksRepository;

    public BooksEntity execute(BooksEntity booksEntity){
        return booksRepository.save(booksEntity);
    }

    public List<BooksEntity> findAllBooks(){
        return booksRepository.findAll();
    }
}
