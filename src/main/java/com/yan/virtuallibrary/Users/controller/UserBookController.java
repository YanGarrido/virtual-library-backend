package com.yan.virtuallibrary.Users.controller;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserBookRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserBookResponseDTO;
import com.yan.virtuallibrary.Users.dto.UserBookUpdateDTO;
import com.yan.virtuallibrary.Users.service.UserBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User Books", description = "Endpoints for managing the authenticated user's personal library")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/users")
public class UserBookController {


    UserBookService userBookService;

    public UserBookController(UserBookService userBookService){
        this.userBookService = userBookService;
    }

    @Operation(summary = "Add book to personal library", description = "Adds an existing catalog book to the authenticated user's library.")
    @ApiResponse(responseCode = "201", description = "Book added to personal library")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @ApiResponse(responseCode = "404", description = "Book or user not found")
    @ApiResponse(responseCode = "409", description = "Book already exists in user's library")
    @PostMapping("/me/books")
    public ResponseEntity<?> addNewBookToUser(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody UserBookRequestDTO userBookRequestDTO){

            UserBookResponseDTO response = this.userBookService.addNewBookToUser(user.getId(),userBookRequestDTO);
            return ResponseEntity.status(201).body(response);

    }
    @Operation(summary = "List personal library", description = "Lists all books from the authenticated user's library.")
    @ApiResponse(responseCode = "200", description = "Personal library retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @GetMapping("/me/books")
    public ResponseEntity<?> findMyBooks(
            @AuthenticationPrincipal UserEntity user){

            var result = this.userBookService.findMyBooks(user.getId());
            return  ResponseEntity.ok().body(result);

    }
    @Operation(summary = "Update personal book status", description = "Updates read status, read format or reading dates of a book in the user's library.")
    @ApiResponse(responseCode = "200", description = "Personal book updated successfully")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @ApiResponse(responseCode = "404", description = "Book not found in user's library")
    @PatchMapping("/me/books/{bookId}")
    public ResponseEntity<?> updateBookStatus(
            @AuthenticationPrincipal UserEntity user,
            @Valid @Parameter(description = "Book id") @PathVariable Long bookId,
            @RequestBody UserBookUpdateDTO userBookUpdateDTO){

        var result = userBookService.updateBookStatus(user.getId(), bookId, userBookUpdateDTO);
        return ResponseEntity.ok().body(result);

    }

    @Operation(summary = "Remove book from personal library", description = "Removes a book from the authenticated user's library.")
    @ApiResponse(responseCode = "204", description = "Book removed from personal library")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @ApiResponse(responseCode = "404", description = "Book not found in user's library")
    @DeleteMapping("/me/books/{bookId}")
    public ResponseEntity<?> deleteBook(
            @AuthenticationPrincipal UserEntity user,
            @Parameter(description = "Book id") @PathVariable Long bookId){
        this.userBookService.deleteBook(user.getId(), bookId);
        return ResponseEntity.noContent().build();
    }
}

