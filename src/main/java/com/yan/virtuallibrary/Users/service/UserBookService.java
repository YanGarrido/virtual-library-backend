package com.yan.virtuallibrary.Users.service;

import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Books.dto.BookResponseDTO;
import com.yan.virtuallibrary.Books.repository.BooksRepository;
import com.yan.virtuallibrary.Users.domain.entities.UserBookEntity;
import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserBookRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserBookResponseDTO;
import com.yan.virtuallibrary.Users.dto.UserBookUpdateDTO;
import com.yan.virtuallibrary.Users.mapper.UserBookMapper;
import com.yan.virtuallibrary.Users.repository.UserBookRepository;
import com.yan.virtuallibrary.Users.repository.UserRepository;
import com.yan.virtuallibrary.common.exception.BookAlreadyInLibraryException;
import com.yan.virtuallibrary.common.exception.BookNotFoundException;
import com.yan.virtuallibrary.common.exception.UnauthorizedException;
import com.yan.virtuallibrary.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBookService {
    private final UserRepository userRepository;
    private final BooksRepository booksRepository;
    private final UserBookRepository userBookRepository;
    private final UserBookMapper userBookMapper;

    public UserBookResponseDTO addNewBookToUser(Long id, UserBookRequestDTO userBookRequestDTO){

        UserEntity user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        BookEntity book = booksRepository.findById(userBookRequestDTO.bookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found!"));

        if(userBookRepository.existsByUser_IdAndBook_Id(id, userBookRequestDTO.bookId())){
            throw new BookAlreadyInLibraryException();
        }

        UserBookEntity userBookEntity = new UserBookEntity();
        userBookEntity.setReadStatus(userBookRequestDTO.readStatus());
        userBookEntity.setReadFormat(userBookRequestDTO.readFormat());
        userBookEntity.setStartedAt(userBookRequestDTO.startedAt());
        userBookEntity.setFinishedAt(userBookRequestDTO.finishedAt());
        userBookEntity.setUser(user);
        userBookEntity.setBook(book);

        UserBookEntity savedUserBook = userBookRepository.save(userBookEntity);

        return userBookMapper.userBookEntityToUserBookResponseDTO(savedUserBook);
    }

    public List<UserBookResponseDTO> findMyBooks(Long id){
        if(id == null){
            throw new UnauthorizedException("User not recognized; please log in.");
        }
         var result = userBookRepository.findAllByUser_Id(id).stream()
                .map(userBookMapper::userBookEntityToUserBookResponseDTO)
                .toList();

        return result;
    }

    public UserBookResponseDTO updateBookStatus(Long userId, Long bookId, UserBookUpdateDTO userBookUpdateDTO){
        UserBookEntity userBook = userBookRepository.findByUser_IdAndBook_Id(userId, bookId)
                .orElseThrow(() -> new BookNotFoundException("You don't have this book on your library!"));

        if(userBookUpdateDTO.readStatus() != null){ userBook.setReadStatus(userBookUpdateDTO.readStatus());}
        if(userBookUpdateDTO.readFormat() != null){ userBook.setReadFormat(userBookUpdateDTO.readFormat());}
        if(userBookUpdateDTO.startedAt() != null){ userBook.setStartedAt(userBookUpdateDTO.startedAt());}
        if(userBookUpdateDTO.finishedAt() != null){ userBook.setFinishedAt(userBookUpdateDTO.finishedAt());}

        UserBookEntity updatedUserBook = userBookRepository.save(userBook);

        return userBookMapper.userBookEntityToUserBookResponseDTO(updatedUserBook);
    }

    public void deleteBook(Long userId, Long bookId){
        UserBookEntity userBook = userBookRepository.findByUser_IdAndBook_Id(userId,bookId)
                .orElseThrow(() -> new BookNotFoundException("You don't have this book on your library!"));
        userBookRepository.delete(userBook);
    }

}

