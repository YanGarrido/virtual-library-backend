package com.yan.virtuallibrary.auth.dto;

import com.yan.virtuallibrary.Users.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(
        @NotBlank() String name,
        @NotBlank() String username,
        @Email() String email,
        @NotBlank() String password,
        Role role
) {
}
