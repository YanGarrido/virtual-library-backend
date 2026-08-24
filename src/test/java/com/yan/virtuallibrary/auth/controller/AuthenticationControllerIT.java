package com.yan.virtuallibrary.auth.controller;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.domain.enums.Role;
import com.yan.virtuallibrary.support.ControllerIntegrationTestSupport;
import com.yan.virtuallibrary.auth.dto.AuthenticationDTO;
import com.yan.virtuallibrary.auth.dto.RegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerIT extends ControllerIntegrationTestSupport {

    @BeforeEach
    void setUp() {
        cleanDatabase();
    }

    @Test
    void register_shouldReturnCreated_whenDataIsValid() throws Exception {
        RegisterDTO request = new RegisterDTO("John Doe", "johndoe", "john.doe@example.com", PASSWORD);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {
        RegisterDTO registerRequest = new RegisterDTO("Jane Doe", "janedoe", "jane.doe@example.com", PASSWORD);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        AuthenticationDTO loginRequest = new AuthenticationDTO("janedoe", PASSWORD);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    void register_shouldReturnBadRequest_whenUsernameAlreadyExists() throws Exception{
        RegisterDTO request = new RegisterDTO("John Doe", "duplicated", "john.doe@example.com", PASSWORD);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        RegisterDTO duplicateRequest = new RegisterDTO("Jane Doe", "duplicated", "jane.doe@example.com", PASSWORD);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username already exists"));
    }

    @Test
    void register_shouldCreateUserRole_whenRequestContainsAdminRole() throws Exception {
        String request = """
                {
                  "name": "Admin Attempt",
                  "username": "adminattempt",
                  "email": "adminattempt@example.com",
                  "password": "123456",
                  "role": "ADMIN"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        UserEntity user = (UserEntity) userRepository.findByUsername("adminattempt");

        assertEquals(Role.USER, user.getRole());
    }
}
