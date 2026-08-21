package com.yan.virtuallibrary.Books.client.dto;

import java.util.List;

public record GoogleBooksVolumeListDTO(
        Integer totalItems,
        List<GoogleBooksVolumeDTO> items
) {
}
