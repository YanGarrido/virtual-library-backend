package com.yan.virtuallibrary.Books.client.dto;

import java.util.List;

public record GoogleBooksVolumeInfoDTO(
        String title,
        List<String> authors,
        String publisher,
        String description,
        List<String> categories,
        List<GoogleBooksIndustryIdentifierDTO> industryIdentifiers,
        GoogleBooksImageLinksDTO imageLinks
) {
}
