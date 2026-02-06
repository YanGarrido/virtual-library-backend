package com.yan.virtuallibrary.Users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank() String name,
        @NotBlank() String username,
        @Email() String email,
        @NotBlank() String password) {
}
