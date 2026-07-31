package com.yan.virtuallibrary.auth.dto;

public record LoginResponseDTO(
        String accessToken,
        String refreshToken,
        Long userId,
        String username,
        String role
) {
}
