package com.yan.virtuallibrary.Users.controller;

import com.yan.virtuallibrary.Users.dto.UserRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserResponseDTO;
import com.yan.virtuallibrary.Users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        try{
            UserResponseDTO response = this.userService.execute(userRequestDTO);
            return ResponseEntity.status(201).body(response);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        try{
            UserResponseDTO response = this.userService.getUserById(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
        return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid  @PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO){
        try{
            UserResponseDTO response = this.userService.updateUser(id, userRequestDTO);
            return ResponseEntity.ok().body(response);

        } catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
