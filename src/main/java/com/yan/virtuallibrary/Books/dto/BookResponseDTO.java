package com.yan.virtuallibrary.Books.dto;

public record BookResponseDTO(
        Long id,
        String externalId,
        String title,
        String author,
        String publisher,
        String isbn,
        String synopsis,
        String genre,
        String coverUrl,
        String source
) {
}
