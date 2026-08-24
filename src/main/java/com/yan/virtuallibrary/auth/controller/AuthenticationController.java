package com.yan.virtuallibrary.auth.controller;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.domain.enums.Role;
import com.yan.virtuallibrary.Users.repository.UserRepository;
import com.yan.virtuallibrary.auth.dto.AuthenticationDTO;
import com.yan.virtuallibrary.auth.dto.LoginResponseDTO;
import com.yan.virtuallibrary.auth.dto.RegisterDTO;
import com.yan.virtuallibrary.common.exception.BadRequestException;
import com.yan.virtuallibrary.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Auth", description = "Endpoints for user registration and authentication")
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponse(responseCode = "200", description = "Login completed successfully")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserEntity) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));

    }

    @Operation(summary = "Register user", description = "Creates a public user account with USER role.")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data or username already exists")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO registerDTO){
        if(this.userRepository.findByUsername(registerDTO.username()) != null){
            throw new BadRequestException("Username already exists");
        }
        String encryptedPassword = this.passwordEncoder.encode(registerDTO.password());
        UserEntity user = new UserEntity(
                registerDTO.name(),
                registerDTO.username(),
                registerDTO.email(),
                encryptedPassword,
                Role.USER
        );
        this.userRepository.save(user);
        return ResponseEntity.status(201).build();
    }
}
