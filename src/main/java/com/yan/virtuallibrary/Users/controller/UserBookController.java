package com.yan.virtuallibrary.Users.controller;

import com.yan.virtuallibrary.Users.dto.UserBookRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserBookResponseDTO;
import com.yan.virtuallibrary.Users.dto.UserBookUpdateDTO;
import com.yan.virtuallibrary.Users.service.UserBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserBookController {
    @Autowired
    UserBookService userBookService;

    @PostMapping("/{id}/books")
    public ResponseEntity<?> addNewBookToUser(@Valid @PathVariable Long id, @RequestBody UserBookRequestDTO userBookRequestDTO){
        try{
            UserBookResponseDTO response = this.userBookService.addNewBookToUser(id,userBookRequestDTO);
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}/books")
    public ResponseEntity<?> findAllBooks(@PathVariable Long id){
        try {
            var result = this.userBookService.findAllBooks(id);
            return  ResponseEntity.ok().body(result);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PatchMapping("/{userId}/books/{bookId}")
    public ResponseEntity<?> updateBookStatus(@PathVariable Long userId, @PathVariable Long bookId, @RequestBody UserBookUpdateDTO userBookUpdateDTO){
        var result = userBookService.updateBookStatus(userId, bookId, userBookUpdateDTO);
        return ResponseEntity.ok().body(result);
    }
}

