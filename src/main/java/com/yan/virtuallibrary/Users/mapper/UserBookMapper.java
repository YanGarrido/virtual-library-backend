package com.yan.virtuallibrary.Users.mapper;

import com.yan.virtuallibrary.Users.domain.entities.UserBookEntity;
import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserBookResponseDTO;
import com.yan.virtuallibrary.Users.dto.UserResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserBookMapper {

    UserBookResponseDTO userBookEntityToUserBookResponseDTO(UserBookEntity userBookEntity);
}
