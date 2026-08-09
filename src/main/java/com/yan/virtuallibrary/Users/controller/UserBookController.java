package com.yan.virtuallibrary.Users.controller;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserBookRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserBookResponseDTO;
import com.yan.virtuallibrary.Users.dto.UserBookUpdateDTO;
import com.yan.virtuallibrary.Users.service.UserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserBookController {


    UserBookService userBookService;

    public UserBookController(UserBookService userBookService){
        this.userBookService = userBookService;
    }

    @PostMapping("/me/books")
    public ResponseEntity<?> addNewBookToUser(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody UserBookRequestDTO userBookRequestDTO){

            UserBookResponseDTO response = this.userBookService.addNewBookToUser(user.getId(),userBookRequestDTO);
            return ResponseEntity.ok().body(response);

    }
    @GetMapping("/me/books")
    public ResponseEntity<?> findMyBooks(
            @AuthenticationPrincipal UserEntity user){

            var result = this.userBookService.findMyBooks(user.getId());
            return  ResponseEntity.ok().body(result);

    }
    @PatchMapping("/me/books/{bookId}")
    public ResponseEntity<?> updateBookStatus(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Long bookId,
            @RequestBody UserBookUpdateDTO userBookUpdateDTO){

        var result = userBookService.updateBookStatus(user.getId(), bookId, userBookUpdateDTO);
        return ResponseEntity.ok().body(result);

    }

    @DeleteMapping("/me/books/{bookId}")
    public ResponseEntity<?> deleteBook(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Long bookId){
        this.userBookService.deleteBook(user.getId(), bookId);
        return ResponseEntity.ok().body("the book in your library was deleted successfully");
    }
}

