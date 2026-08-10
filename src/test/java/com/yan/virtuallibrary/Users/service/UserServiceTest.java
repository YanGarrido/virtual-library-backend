package com.yan.virtuallibrary.Users.service;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.domain.enums.Role;
import com.yan.virtuallibrary.Users.dto.UserResponseDTO;
import com.yan.virtuallibrary.Users.dto.UserUpdateDTO;
import com.yan.virtuallibrary.Users.repository.UserRepository;
import com.yan.virtuallibrary.common.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    public UserService userService;


    @Test
    void getMe_shouldReturnUserResponse_whenUserExists() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("John Doe");
        userEntity.setUsername("johndoe");
        userEntity.setEmail("johndoe@example.com");
        userEntity.setRole(Role.USER);

        UserResponseDTO reponse = userService.getMe(userEntity);
        assertEquals("John Doe", reponse.name());
        assertEquals("johndoe", reponse.username());
        assertEquals("johndoe@example.com", reponse.email());
        assertEquals("USER", reponse.role());
    }
    @Test
    void getMe_shouldThrowNewUserNotFoundException_whenUserIsNull(){
        assertThrows(UserNotFoundException.class, () -> userService.getMe(null));
    }

    @Test
    void updateUser_shouldUpdateName_whenNameIsProvided() {
        Long id = 1L;

        UserEntity user = new UserEntity();
        user.setName("Jane Doe");
        user.setUsername("janedoe");
        user.setEmail("janedoe@example.com");
        user.setRole(Role.USER);

        UserUpdateDTO dto = new UserUpdateDTO(
                "Jane Smith",
                null,
                null,
                null);

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        UserResponseDTO response = userService.updateUser(id, dto);

        assertEquals("Jane Smith", response.name());
        assertEquals("janedoe", response.username());
        assertEquals("janedoe@example.com", response.email());

        verify(userRepository).save(user);





    }
    @Test
    void updateUser_shouldEncodePassword_whenPasswordIsProvided() {
        Long id = 1L;

        UserEntity user = new UserEntity();
        user.setName("Jane Doe");
        user.setUsername("janedoe");
        user.setEmail("janedoe@example.com");
        user.setRole(Role.USER);
        user.setPassword("oldPassword");

        UserUpdateDTO dto = new UserUpdateDTO(
                null,
                null,
                null,
                "newPassword");

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("encodedNewPassword");

        when(userRepository.save(user)).thenReturn(user);

        UserResponseDTO response = userService.updateUser(id, dto);

        assertEquals("Jane Doe", response.name());
        assertEquals("janedoe", response.username());
        assertEquals("janedoe@example.com", response.email());
        assertEquals("USER", response.role());
        assertEquals("encodedNewPassword", user.getPassword());

        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(user);

    }
}


