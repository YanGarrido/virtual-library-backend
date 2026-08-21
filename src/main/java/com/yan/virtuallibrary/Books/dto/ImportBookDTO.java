package com.yan.virtuallibrary.Books.dto;

import jakarta.validation.constraints.NotBlank;

public record ImportBookDTO(
        @NotBlank
        String externalId
) {
}
