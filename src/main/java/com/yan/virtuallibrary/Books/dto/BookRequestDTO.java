package com.yan.virtuallibrary.Books.dto;

import jakarta.validation.constraints.NotBlank;

public record BookRequestDTO(
        String externalId,
        @NotBlank String title,
        String author,
        String publisher,
        String isbn,
        String synopsis,
        String genre,
        String coverUrl
) {
}
