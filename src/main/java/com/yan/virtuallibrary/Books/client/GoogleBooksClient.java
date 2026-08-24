package com.yan.virtuallibrary.Books.client;

import com.yan.virtuallibrary.Books.client.dto.GoogleBooksVolumeDTO;
import com.yan.virtuallibrary.Books.client.dto.GoogleBooksVolumeListDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GoogleBooksClient {

    private final RestClient.Builder restClientBuilder;
    private final String baseUrl;
    private final String apiKey;

    public GoogleBooksClient(
            RestClient.Builder restClientBuilder,
            @Value("${external.google-books.base-url:https://www.googleapis.com/books/v1}") String baseUrl,
            @Value("${external.google-books.api-key:}") String apiKey
    ) {
        this.restClientBuilder = restClientBuilder;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public GoogleBooksVolumeListDTO searchBooks(String query, Integer maxResults) {
        return restClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/volumes")
                        .queryParam("q", query)
                        .queryParam("maxResults", maxResults)
                        .queryParam("printType", "books")
                        .queryParam("projection", "lite")
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .body(GoogleBooksVolumeListDTO.class);
    }

    public GoogleBooksVolumeDTO findBookById(String externalId) {
        return restClient().get().uri(uriBuilder -> uriBuilder
                .path("/volumes/{externalId}")
                .queryParam("key", apiKey)
                .build(externalId))
                .retrieve()
                .body(GoogleBooksVolumeDTO.class);
    }

    private RestClient restClient() {
        return restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }
}
