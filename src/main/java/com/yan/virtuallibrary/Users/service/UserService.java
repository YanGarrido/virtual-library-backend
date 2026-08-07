package com.yan.virtuallibrary.Users.service;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserResponseDTO;
import com.yan.virtuallibrary.Users.dto.UserUpdateDTO;
import com.yan.virtuallibrary.Users.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        UserEntity user = new UserEntity(
                userRequestDTO.name(),
                userRequestDTO.username(),
                userRequestDTO.email(),
                userRequestDTO.password(),
                userRequestDTO.role()
        );


        UserEntity savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getName(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name());
    }

    public UserResponseDTO getMe(UserEntity user) {
      return new UserResponseDTO(
              user.getName(),
              user.getUsername(),
              user.getEmail(),
              user.getRole().name());
    }

    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if(userUpdateDTO.name() != null) {
            user.setName(userUpdateDTO.name());
        }
        if(userUpdateDTO.username() != null) {
            user.setUsername(userUpdateDTO.username());
        }
        if(userUpdateDTO.email() != null) {
            user.setEmail(userUpdateDTO.email());
        }
        if(userUpdateDTO.password() != null) {
            user.setPassword(userUpdateDTO.password());
        }
        UserEntity updatedUser = userRepository.save(user);
        return new UserResponseDTO(
                updatedUser.getName(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getRole().name());
    }

    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }


}


