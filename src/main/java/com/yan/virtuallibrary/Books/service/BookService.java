package com.yan.virtuallibrary.Books.service;

import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Books.dto.BookRequestDTO;
import com.yan.virtuallibrary.Books.dto.BookResponseDTO;
import com.yan.virtuallibrary.Books.dto.BookSearchResponseDTO;
import com.yan.virtuallibrary.Books.repository.BooksRepository;
import com.yan.virtuallibrary.common.exception.BookNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private BooksRepository booksRepository;

    public BookService(BooksRepository booksRepository){
        this.booksRepository = booksRepository;
    }

    private BookResponseDTO convertBookEntityforBookResponseDTO(BookEntity bookEntity){
        return new BookResponseDTO(
                bookEntity.getId(),
                bookEntity.getExternalId(),
                bookEntity.getTitle(),
                bookEntity.getAuthor(),
                bookEntity.getPublisher(),
                bookEntity.getIsbn(),
                bookEntity.getSynopsis(),
                bookEntity.getGenre(),
                bookEntity.getCoverUrl(),
                bookEntity.getSource().name());

    }
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

        return convertBookEntityforBookResponseDTO(bookEntity);
    }

    public List<BookEntity> findAllBooks(){
        return booksRepository.findAll();
    }

    public BookResponseDTO findBook(Long bookId){
        BookEntity book = booksRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found!"));
        return convertBookEntityforBookResponseDTO(book);
    }

    public BookResponseDTO updateBooks(Long id, BookRequestDTO bookRequestDTO) {
        BookEntity bookEntity = booksRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
        if(bookEntity != null){
            bookEntity.setAuthor(bookRequestDTO.author());
            bookEntity.setTitle(bookRequestDTO.title());
            bookEntity.setPublisher(bookRequestDTO.publisher());
            bookEntity.setIsbn(bookRequestDTO.isbn());
            bookEntity.setSynopsis(bookRequestDTO.synopsis());
            bookEntity.setGenre(bookRequestDTO.genre());
            bookEntity.setCoverUrl(bookRequestDTO.coverUrl());
        }
        BookEntity updateBook = booksRepository.save(bookEntity);
        return convertBookEntityforBookResponseDTO(updateBook);

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
}
