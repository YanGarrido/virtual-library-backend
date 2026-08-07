package com.yan.virtuallibrary.Books.controller;

import com.yan.virtuallibrary.Books.dto.BookRequestDTO;
import com.yan.virtuallibrary.Books.service.BookService;
import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BooksController {

    private BookService bookService;

    public BooksController(BookService bookService){
        this.bookService = bookService;
    }

    @PostMapping()
    public ResponseEntity<Object> createBook(@RequestBody BookEntity bookEntity){
        try{
            var result = bookService.execute(bookEntity);
            return ResponseEntity.ok().body(result);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> findAllBooks(){
        try {
            var result = bookService.findAllBooks();
            return  ResponseEntity.ok().body(result);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{bookId}")
    public ResponseEntity<?> findBook(@PathVariable Long bookId){
            var result = bookService.findBook(bookId);
            return  ResponseEntity.ok().body(result);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable Long bookId, @RequestBody BookRequestDTO bookRequestDTO){
        this.bookService.updateBooks(bookId, bookRequestDTO);
        return ResponseEntity.ok().body("Book updated successfully");
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId){
        this.bookService.deleteBook(bookId);
        return ResponseEntity.ok().body("Book deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<?> findBooks(@RequestParam(required = false) String title,
                                       @RequestParam(required = false) String author,
                                       @RequestParam(required = false) String genre,
                                       @RequestParam(required = false) String isbn){
    var result = this.bookService.findBooks(title, author, genre, isbn);
    return ResponseEntity.ok().body(result);
    }
}
