package com.yan.virtuallibrary.Users.controller;

import com.yan.virtuallibrary.Users.dto.UserBookRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserBookResponseDTO;
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
}
