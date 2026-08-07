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
    @Autowired
    UserBookService userBookService;

    @PostMapping("/me/books")
    public ResponseEntity<?> addNewBookToUser(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody UserBookRequestDTO userBookRequestDTO){

        try{
            UserBookResponseDTO response = this.userBookService.addNewBookToUser(user.getId(),userBookRequestDTO);
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/me/books")
    public ResponseEntity<?> findMyBooks(
            @AuthenticationPrincipal UserEntity user){

        try {
            var result = this.userBookService.findMyBooks(user.getId());
            return  ResponseEntity.ok().body(result);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PatchMapping("/me/books/{bookId}")
    public ResponseEntity<?> updateBookStatus(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Long bookId,
            @RequestBody UserBookUpdateDTO userBookUpdateDTO){

        var result = userBookService.updateBookStatus(user.getId(), bookId, userBookUpdateDTO);
        return ResponseEntity.ok().body(result);
    }
}

