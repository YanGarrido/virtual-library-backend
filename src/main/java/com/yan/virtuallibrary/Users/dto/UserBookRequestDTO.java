package com.yan.virtuallibrary.Users.dto;

import com.yan.virtuallibrary.Users.domain.enums.ReadFormat;
import com.yan.virtuallibrary.Users.domain.enums.ReadStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserBookRequestDTO(
        @NotNull Long bookId,
        @NotNull ReadStatus readStatus,
        @NotNull ReadFormat readFormat,
        LocalDate startedAt,
        LocalDate finishedAt
        ) {
}
