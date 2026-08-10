package com.yan.virtuallibrary.Users.controller;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserUpdateDTO;

import com.yan.virtuallibrary.Users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

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

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(
            @AuthenticationPrincipal UserEntity user){

            this.userService.deleteUser(user.getId());
            return ResponseEntity.noContent().build();
    }

}
