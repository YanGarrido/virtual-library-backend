package com.yan.virtuallibrary.Users.controller;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserUpdateDTO;

import com.yan.virtuallibrary.Users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Users", description = "Endpoints for managing the authenticated user account")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Get authenticated user", description = "Returns the profile data of the authenticated user.")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @GetMapping("/me")
    public ResponseEntity<?> getMe(
            @AuthenticationPrincipal UserEntity user){

        var result = this.userService.getMe(user);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Update authenticated user", description = "Updates profile data of the authenticated user.")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PatchMapping("/me")
    public ResponseEntity<?> updateMe(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody UserUpdateDTO userUpdateDTO){

        var result = this.userService.updateUser(user.getId(),userUpdateDTO);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Delete authenticated user", description = "Deletes the authenticated user account.")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(
            @AuthenticationPrincipal UserEntity user){

            this.userService.deleteUser(user.getId());
            return ResponseEntity.noContent().build();
    }

}
