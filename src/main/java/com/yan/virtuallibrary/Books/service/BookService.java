package com.yan.virtuallibrary.Books.service;

import com.yan.virtuallibrary.Books.client.GoogleBooksClient;
import com.yan.virtuallibrary.Books.client.dto.GoogleBooksIndustryIdentifierDTO;
import com.yan.virtuallibrary.Books.client.dto.GoogleBooksVolumeDTO;
import com.yan.virtuallibrary.Books.client.dto.GoogleBooksVolumeInfoDTO;
import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Books.domain.enums.BookSource;
import com.yan.virtuallibrary.Books.dto.BookRequestDTO;
import com.yan.virtuallibrary.Books.dto.BookResponseDTO;
import com.yan.virtuallibrary.Books.dto.BookSearchResponseDTO;
import com.yan.virtuallibrary.Books.dto.ImportBookDTO;
import com.yan.virtuallibrary.Books.mapper.BookMapper;
import com.yan.virtuallibrary.Books.repository.BooksRepository;
import com.yan.virtuallibrary.common.exception.BookNotFoundException;
import com.yan.virtuallibrary.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BooksRepository booksRepository;
    private final GoogleBooksClient googleBooksClient;

    public BookResponseDTO createBook(BookRequestDTO bookRequest){
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle(bookRequest.title());
        bookEntity.setAuthor(bookRequest.author());
        bookEntity.setCoverUrl(bookRequest.coverUrl());
        bookEntity.setIsbn(bookRequest.isbn());
        bookEntity.setExternalId(bookRequest.externalId());
        bookEntity.setGenre(bookRequest.genre());
        bookEntity.setPublisher(bookRequest.publisher());
        bookEntity.setSynopsis(bookRequest.synopsis());

        booksRepository.save(bookEntity);

        return bookMapper.bookEntityToBookResponseDTO(bookEntity);
    }

    public List<BookResponseDTO> findAllBooks(){
        return booksRepository.findAll().stream()
                .map(bookMapper::bookEntityToBookResponseDTO)
                .toList();
    }

    public BookResponseDTO findBook(Long bookId){
        BookEntity book = booksRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found!"));
        return bookMapper.bookEntityToBookResponseDTO(book);
    }

    public BookResponseDTO updateBooks(Long id, BookRequestDTO bookRequestDTO) {
        BookEntity bookEntity = booksRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
            bookEntity.setAuthor(bookRequestDTO.author());
            bookEntity.setTitle(bookRequestDTO.title());
            bookEntity.setPublisher(bookRequestDTO.publisher());
            bookEntity.setIsbn(bookRequestDTO.isbn());
            bookEntity.setSynopsis(bookRequestDTO.synopsis());
            bookEntity.setGenre(bookRequestDTO.genre());
            bookEntity.setCoverUrl(bookRequestDTO.coverUrl());

        BookEntity updateBook = booksRepository.save(bookEntity);
        return bookMapper.bookEntityToBookResponseDTO(updateBook);

    }
    public void deleteBook(Long id) {
        BookEntity bookEntity = booksRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
        booksRepository.delete(bookEntity);
    }

    public List<BookSearchResponseDTO> findBooks(String title, String author, String genre, String isbn) {
        List<BookEntity> books = booksRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenreContainingIgnoreCaseAndIsbnContainingIgnoreCase(
                title != null ? title : "",
                author != null ? author : "",
                genre != null ? genre : "",
                isbn != null ? isbn : ""
        );

        return books.stream()
                .map(book -> new BookSearchResponseDTO(
                        book.getExternalId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getIsbn(),
                        book.getSynopsis(),
                        book.getGenre(),
                        book.getCoverUrl()
                ))
                .toList();
    }

    public List<BookSearchResponseDTO> searchExternalBooks(String query, Integer maxResults) {
        if (query == null || query.isBlank()) {
            throw new BadRequestException("Search query is required");
        }

        int safeMaxResults = maxResults == null ? 10 : Math.clamp(maxResults, 1, 40);
        var response = googleBooksClient.searchBooks(query, safeMaxResults);

        if (response == null || response.items() == null) {
            return Collections.emptyList();
        }

        return response.items().stream()
                .map(this::convertGoogleVolumeToBookSearchResponse)
                .toList();
    }

    public BookResponseDTO importExternalBook(ImportBookDTO importBookDTO) {
        return booksRepository.findByExternalIdAndSource(importBookDTO.externalId(), BookSource.GOOGLE_BOOKS)
                .map(bookMapper::bookEntityToBookResponseDTO)
                .orElseGet(() -> createBookFromGoogleBooks(importBookDTO.externalId()));
    }

    private BookResponseDTO createBookFromGoogleBooks(String externalId) {
        GoogleBooksVolumeDTO volume = googleBooksClient.findBookById(externalId);

        if (volume == null || volume.volumeInfo() == null) {
            throw new BookNotFoundException("External book not found");
        }

        BookEntity bookEntity = convertGoogleVolumeToBookEntity(volume);
        BookEntity savedBook = booksRepository.save(bookEntity);

        return bookMapper.bookEntityToBookResponseDTO(savedBook);
    }

    private BookSearchResponseDTO convertGoogleVolumeToBookSearchResponse(GoogleBooksVolumeDTO volume) {
        GoogleBooksVolumeInfoDTO info = volume.volumeInfo();

        return new BookSearchResponseDTO(
                volume.id(),
                info != null ? info.title() : null,
                formatAuthors(info),
                info != null ? info.publisher() : null,
                findBestIsbn(info),
                info != null ? info.description() : null,
                findFirstCategory(info),
                findCoverUrl(info)
        );
    }

    private BookEntity convertGoogleVolumeToBookEntity(GoogleBooksVolumeDTO volume) {
        GoogleBooksVolumeInfoDTO info = volume.volumeInfo();

        BookEntity bookEntity = new BookEntity();
        bookEntity.setExternalId(volume.id());
        bookEntity.setTitle(info.title());
        bookEntity.setAuthor(formatAuthors(info));
        bookEntity.setPublisher(info.publisher());
        bookEntity.setIsbn(findBestIsbn(info));
        bookEntity.setSynopsis(info.description());
        bookEntity.setGenre(findFirstCategory(info));
        bookEntity.setCoverUrl(findCoverUrl(info));
        bookEntity.setSource(BookSource.GOOGLE_BOOKS);

        return bookEntity;
    }

    private String formatAuthors(GoogleBooksVolumeInfoDTO info) {
        if (info == null || info.authors() == null || info.authors().isEmpty()) {
            return null;
        }

        return String.join(", ", info.authors());
    }

    private String findFirstCategory(GoogleBooksVolumeInfoDTO info) {
        if (info == null || info.categories() == null || info.categories().isEmpty()) {
            return null;
        }

        return info.categories().getFirst();
    }

    private String findCoverUrl(GoogleBooksVolumeInfoDTO info) {
        if (info == null || info.imageLinks() == null) {
            return null;
        }

        return info.imageLinks().thumbnail() != null
                ? info.imageLinks().thumbnail()
                : info.imageLinks().smallThumbnail();
    }

    private String findBestIsbn(GoogleBooksVolumeInfoDTO info) {
        if (info == null || info.industryIdentifiers() == null || info.industryIdentifiers().isEmpty()) {
            return null;
        }

        return info.industryIdentifiers().stream()
                .filter(identifier -> "ISBN_13".equals(identifier.type()))
                .map(GoogleBooksIndustryIdentifierDTO::identifier)
                .findFirst()
                .orElseGet(() -> info.industryIdentifiers().stream()
                        .filter(identifier -> "ISBN_10".equals(identifier.type()))
                        .map(GoogleBooksIndustryIdentifierDTO::identifier)
                        .findFirst()
                        .orElse(info.industryIdentifiers().getFirst().identifier()));
    }
}
