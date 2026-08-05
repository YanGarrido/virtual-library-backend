package com.yan.virtuallibrary.Books.service;

import com.yan.virtuallibrary.Books.domain.entities.BooksEntity;
import com.yan.virtuallibrary.Books.dto.BookRequestDTO;
import com.yan.virtuallibrary.Books.dto.BookResponseDTO;
import com.yan.virtuallibrary.Books.dto.BookSearchResponseDTO;
import com.yan.virtuallibrary.Books.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksService {

    @Autowired
    private BooksRepository booksRepository;

    public BooksEntity execute(BooksEntity booksEntity){
        return booksRepository.save(booksEntity);
    }

    public List<BooksEntity> findAllBooks(){
        return booksRepository.findAll();
    }

    public BookResponseDTO updateBooks(Long id, BookRequestDTO bookRequestDTO) {
        BooksEntity booksEntity = booksRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        if(booksEntity != null){
            booksEntity.setAuthor(bookRequestDTO.author());
            booksEntity.setTitle(bookRequestDTO.title());
            booksEntity.setPublisher(bookRequestDTO.publisher());
            booksEntity.setIsbn(bookRequestDTO.isbn());
            booksEntity.setSynopsis(bookRequestDTO.synopsis());
            booksEntity.setGenre(bookRequestDTO.genre());
            booksEntity.setCoverUrl(bookRequestDTO.coverUrl());

            BooksEntity updateBook = booksRepository.save(booksEntity);

            return new BookResponseDTO(
                    booksEntity.getId(),
                    booksEntity.getExternalId(),
                    booksEntity.getTitle(),
                    booksEntity.getAuthor(),
                    booksEntity.getPublisher(),
                    booksEntity.getIsbn(),
                    booksEntity.getSynopsis(),
                    booksEntity.getGenre(),
                    booksEntity.getCoverUrl(),
                    booksEntity.getSource().name()
            );
        } else {
            throw new RuntimeException("User not found");
        }

    }
    public void deleteBook(Long id) {
        BooksEntity booksEntity = booksRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        booksRepository.delete(booksEntity);
    }

    public List<BookSearchResponseDTO> findBooks(String title, String author, String genre, String isbn) {
        List<BooksEntity> books = booksRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndGenreContainingIgnoreCaseAndIsbnContainingIgnoreCase(
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
