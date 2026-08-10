package com.yan.virtuallibrary.Users.service;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.dto.UserResponseDTO;
import com.yan.virtuallibrary.Users.dto.UserUpdateDTO;
import com.yan.virtuallibrary.Users.repository.UserRepository;
import com.yan.virtuallibrary.common.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }
    private UserResponseDTO convertUserEntityforUserResponse(UserEntity user){
        return new UserResponseDTO(
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name());
    }

    public UserResponseDTO getMe(UserEntity user) {
        if(user == null){
            throw new UserNotFoundException();
        }
      return convertUserEntityforUserResponse(user);
    }

    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
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
            String encryptedPassword = this.passwordEncoder.encode(userUpdateDTO.password());
            user.setPassword(encryptedPassword);
        }
        UserEntity updatedUser = userRepository.save(user);
        return convertUserEntityforUserResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }


}


