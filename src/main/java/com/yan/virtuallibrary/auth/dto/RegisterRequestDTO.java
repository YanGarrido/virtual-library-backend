package com.yan.virtuallibrary.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
        @NotBlank String name,
        @NotBlank String username,
        @NotBlank String email,
        @NotBlank String password
) {
}
