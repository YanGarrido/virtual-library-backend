package com.yan.virtuallibrary.Books.mapper;

import com.yan.virtuallibrary.Books.domain.entities.BookEntity;
import com.yan.virtuallibrary.Books.dto.BookResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookResponseDTO bookEntityToBookResponseDTO(BookEntity bookEntity);
}
