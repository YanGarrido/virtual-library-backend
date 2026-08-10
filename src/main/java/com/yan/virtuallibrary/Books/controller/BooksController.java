package com.yan.virtuallibrary.Books.controller;

import com.yan.virtuallibrary.Books.dto.BookRequestDTO;
import com.yan.virtuallibrary.Books.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;

    public BooksController(BookService bookService){
        this.bookService = bookService;
    }

    @PostMapping()
    public ResponseEntity<Object> createBook(@Valid @RequestBody BookRequestDTO bookRequestDTO){
            var result = bookService.createBook(bookRequestDTO);
            return ResponseEntity.status(201).body(result);
    }

    @GetMapping()
    public ResponseEntity<?> findAllBooks(){
            var result = bookService.findAllBooks();
            return  ResponseEntity.ok().body(result);
    }
    @GetMapping("/{bookId}")
    public ResponseEntity<?> findBook(@PathVariable Long bookId){
            var result = bookService.findBook(bookId);
            return  ResponseEntity.ok().body(result);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable Long bookId, @Valid @RequestBody BookRequestDTO bookRequestDTO){
        var response = this.bookService.updateBooks(bookId, bookRequestDTO);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId){
        this.bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
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
