package com.yan.virtuallibrary.Users.dto;

public record UserProfileDTO(
        Long id,
        String name,
        String username,
        String email,
        String role
) {
}
