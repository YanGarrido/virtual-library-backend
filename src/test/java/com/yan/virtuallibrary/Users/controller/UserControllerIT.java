package com.yan.virtuallibrary.Users.controller;

import com.yan.virtuallibrary.Users.domain.enums.Role;
import com.yan.virtuallibrary.Users.dto.UserUpdateDTO;
import com.yan.virtuallibrary.support.ControllerIntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIT extends ControllerIntegrationTestSupport {

    @BeforeEach
    void setUp() {
        cleanDatabase();
    }

    @Test
    void getMe_shouldReturnForbidden_whenTokenIsMissing() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getMe_shouldReturnAuthenticatedUser_whenTokenIsValid() throws Exception {
        String token = createUserAndGenerateToken("profileuser", "profile@example.com", Role.USER);

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("profileuser"))
                .andExpect(jsonPath("$.email").value("profile@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void updateMe_shouldReturnUpdatedUser_whenTokenIsValid() throws Exception {
        String token = createUserAndGenerateToken("updateprofile", "updateprofile@example.com", Role.USER);
        UserUpdateDTO request = new UserUpdateDTO(
                "Updated User",
                "updatedprofile",
                "updatedprofile@example.com",
                null
        );

        mockMvc.perform(patch("/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.username").value("updatedprofile"))
                .andExpect(jsonPath("$.email").value("updatedprofile@example.com"));
    }

    @Test
    void deleteMe_shouldReturnNoContent_whenTokenIsValid() throws Exception {
        String token = createUserAndGenerateToken("deleteprofile", "deleteprofile@example.com", Role.USER);

        mockMvc.perform(delete("/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
