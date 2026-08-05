package com.yan.virtuallibrary.Books.dto;

import java.time.LocalDateTime;

public record ReviewResponseDTO(
        Long id,
        Integer rating,
        String text,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
