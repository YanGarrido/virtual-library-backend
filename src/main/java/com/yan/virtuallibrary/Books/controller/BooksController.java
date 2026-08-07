package com.yan.virtuallibrary.Books.controller;

import com.yan.virtuallibrary.Books.dto.BookRequestDTO;
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

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBooks(@PathVariable Long bookId, @RequestBody BookRequestDTO bookRequestDTO){
        this.booksService.updateBooks(bookId, bookRequestDTO);
        return ResponseEntity.ok().body("Book updated successfully");
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBooks(@PathVariable Long bookId){
        this.booksService.deleteBook(bookId);
        return ResponseEntity.ok().body("Book deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<?> findBooks(@RequestParam(required = false) String title,
                                       @RequestParam(required = false) String author,
                                       @RequestParam(required = false) String genre,
                                       @RequestParam(required = false) String isbn){
    var result = this.booksService.findBooks(title, author, genre, isbn);
    return ResponseEntity.ok().body(result);
    }
}
