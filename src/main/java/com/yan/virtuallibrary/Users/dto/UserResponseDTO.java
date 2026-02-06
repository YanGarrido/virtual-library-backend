package com.yan.virtuallibrary.Users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserResponseDTO(
        Long id,
        String name,
        String username,
        String email) {
}
