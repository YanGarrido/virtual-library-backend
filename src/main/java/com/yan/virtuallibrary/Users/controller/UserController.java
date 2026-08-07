package com.yan.virtuallibrary.Users.controller;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserResponseDTO;
import com.yan.virtuallibrary.Users.dto.UserUpdateDTO;
import com.yan.virtuallibrary.Users.service.UserBookService;
import com.yan.virtuallibrary.Users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserBookService userBookService;

    @GetMapping("/me")
    public ResponseEntity<?> getMe(
            @AuthenticationPrincipal UserEntity user){

        var result = this.userService.getMe(user);
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateMe(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody UserUpdateDTO userUpdateDTO){

        var result = this.userService.updateUser(user.getId(),userUpdateDTO);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id){

        try{
            this.userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted successfully");

        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
