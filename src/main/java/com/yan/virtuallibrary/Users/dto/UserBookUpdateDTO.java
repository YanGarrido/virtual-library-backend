package com.yan.virtuallibrary.Users.dto;

import com.yan.virtuallibrary.Users.domain.enums.ReadFormat;
import com.yan.virtuallibrary.Users.domain.enums.ReadStatus;

import java.time.LocalDate;

public record UserBookUpdateDTO(
        ReadStatus readStatus,
        ReadFormat readFormat,
        LocalDate startedAt,
        LocalDate finishedAt
) {
}
