package com.yan.virtuallibrary.Users.dto;

import com.yan.virtuallibrary.Books.dto.BookResponseDTO;
import com.yan.virtuallibrary.Users.domain.enums.ReadFormat;
import com.yan.virtuallibrary.Users.domain.enums.ReadStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserBookResponseDTO(
        Long id,
        BookResponseDTO book,
        ReadStatus readStatus,
        ReadFormat readFormat,
        LocalDate startedAt,
        LocalDate finishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
