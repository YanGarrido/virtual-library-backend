package com.yan.virtuallibrary.Books.controller;

import com.yan.virtuallibrary.Books.dto.BookRequestDTO;
import com.yan.virtuallibrary.Books.dto.ImportBookDTO;
import com.yan.virtuallibrary.Books.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Books", description = "Endpoints for managing books in the virtual library")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;

    public BooksController(BookService bookService){
        this.bookService = bookService;
    }

    @Operation(summary = "Create a new book", description = "Creates a new book in the virtual library. Requires ADMIN role.")
    @ApiResponse(responseCode = "201", description = "Book created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid book data")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @PostMapping()
    public ResponseEntity<Object> createBook(@Valid @RequestBody BookRequestDTO bookRequestDTO){
            var result = bookService.createBook(bookRequestDTO);
            return ResponseEntity.status(201).body(result);
    }

    @Operation(summary = "Find all books", description = "Retrieves a list of all books in the virtual library.")
    @ApiResponse(responseCode = "200", description = "List of books retrieved successfully")
    @GetMapping()
    public ResponseEntity<?> findAllBooks(){
            var result = bookService.findAllBooks();
            return  ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Find book by id", description = "Retrieves a single book from the catalog by id.")
    @ApiResponse(responseCode = "200", description = "Book retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @GetMapping("/{bookId}")
    public ResponseEntity<?> findBook(@Parameter(description = "Book id") @PathVariable Long bookId){
            var result = bookService.findBook(bookId);
            return  ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Update book", description = "Updates a catalog book. Requires ADMIN role.")
    @ApiResponse(responseCode = "200", description = "Book updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid book data")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@Parameter(description = "Book id") @PathVariable Long bookId, @Valid @RequestBody BookRequestDTO bookRequestDTO){
        var response = this.bookService.updateBooks(bookId, bookRequestDTO);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Delete book", description = "Removes a catalog book. Requires ADMIN role.")
    @ApiResponse(responseCode = "204", description = "Book deleted successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@Parameter(description = "Book id") @PathVariable Long bookId){
        this.bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search books", description = "Searches catalog books by optional filters.")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @GetMapping("/search")
    public ResponseEntity<?> findBooks(@Parameter(description = "Title filter") @RequestParam(required = false) String title,
                                       @Parameter(description = "Author filter") @RequestParam(required = false) String author,
                                       @Parameter(description = "Genre filter") @RequestParam(required = false) String genre,
                                       @Parameter(description = "ISBN filter") @RequestParam(required = false) String isbn){
    var result = this.bookService.findBooks(title, author, genre, isbn);
    return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Search external books", description = "Searches books in the Google Books API.")
    @ApiResponse(responseCode = "200", description = "External search completed successfully")
    @ApiResponse(responseCode = "400", description = "Search query is required")
    @GetMapping("/external/search")
    public ResponseEntity<?> searchExternalBooks(@Parameter(description = "Search term") @RequestParam String query,
                                                 @Parameter(description = "Maximum number of results, between 1 and 40") @RequestParam(required = false) Integer maxResults) {
        var result = this.bookService.searchExternalBooks(query, maxResults);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Import external book", description = "Imports a Google Books volume into the local catalog. Requires ADMIN role.")
    @ApiResponse(responseCode = "201", description = "External book imported successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "404", description = "External book not found")
    @PostMapping("/import")
    public ResponseEntity<?> importExternalBook(@Valid @RequestBody ImportBookDTO importBookDTO) {
        var result = this.bookService.importExternalBook(importBookDTO);
        return ResponseEntity.status(201).body(result);
    }
}
