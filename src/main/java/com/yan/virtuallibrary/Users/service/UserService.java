package com.yan.virtuallibrary.Users.service;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserRequestDTO;
import com.yan.virtuallibrary.Users.dto.UserResponseDTO;
import com.yan.virtuallibrary.Users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name());
    }

    public UserResponseDTO getUserById(Long id) {
       UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name());


    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (user != null) {
            user.setName(userRequestDTO.name());
            user.setUsername(userRequestDTO.username());
            user.setEmail(userRequestDTO.email());
            user.setPassword(userRequestDTO.password());

            UserEntity updatedUser = userRepository.save(user);

            return new UserResponseDTO(
                    updatedUser.getId(),
                    updatedUser.getName(),
                    updatedUser.getUsername(),
                    updatedUser.getEmail(),
                    updatedUser.getRole().name());
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}


