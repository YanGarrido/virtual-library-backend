package com.yan.virtuallibrary.Users.dto;

public record UserResponseDTO(
        String name,
        String username,
        String email,
        String role
) {
}
