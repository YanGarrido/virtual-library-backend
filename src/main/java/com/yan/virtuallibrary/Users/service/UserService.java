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

    public UserResponseDTO execute(UserRequestDTO userRequestDTO) {
        UserEntity user = new UserEntity();
        user.setName(userRequestDTO.name());
        user.setUsername(userRequestDTO.username());
        user.setEmail(userRequestDTO.email());
        user.setPassword(userRequestDTO.password());

        UserEntity savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getUsername(),
                savedUser.getEmail());
    }

    public UserResponseDTO getUserById(Long id){
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isPresent()){
            return new UserResponseDTO(
                    user.get().getId(),
                    user.get().getName(),
                    user.get().getUsername(),
                    user.get().getEmail());

        } else{
            throw new RuntimeException("User not found");
        }


    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO){
        Optional<UserEntity> user = userRepository.findById(id);
        if(user.isPresent()){
            UserEntity userExisting = user.get();
            userExisting.setName(userRequestDTO.name());
            userExisting.setUsername(userRequestDTO.username());
            userExisting.setEmail(userRequestDTO.email());
            userExisting.setPassword(userRequestDTO.password());

            UserEntity updatedUser = userRepository.save(userExisting);

            return new UserResponseDTO(
                    updatedUser.getId(),
                    updatedUser.getName(),
                    updatedUser.getUsername(),
                    updatedUser.getEmail());
        } else {
            throw new RuntimeException("User not found");
        }
    }

}
