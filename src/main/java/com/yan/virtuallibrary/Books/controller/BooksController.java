package com.yan.virtuallibrary.Books.controller;

import com.yan.virtuallibrary.Books.service.BooksService;
import com.yan.virtuallibrary.Books.domain.entities.BooksEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private BooksService booksService;

    @PostMapping("/")
    public ResponseEntity<Object> createBooks(@RequestBody BooksEntity booksEntity){
        try{
            var result = booksService.execute(booksEntity);
            return ResponseEntity.ok().body(result);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> findAllBooks(){
        try {
            var result = booksService.findAllBooks();
            return  ResponseEntity.ok().body(result);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
