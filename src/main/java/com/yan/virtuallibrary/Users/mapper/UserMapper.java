package com.yan.virtuallibrary.Users.mapper;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO userEntityToUserResponseDTO(UserEntity userEntity);
}
