package com.yan.virtuallibrary.Users.dto;

public record UserUpdateDTO(
        String name,
        String username,
        String email,
        String password) {
}
