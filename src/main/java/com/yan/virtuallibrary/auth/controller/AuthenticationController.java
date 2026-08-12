package com.yan.virtuallibrary.auth.controller;

import com.yan.virtuallibrary.Users.domain.entities.UserEntity;
import com.yan.virtuallibrary.Users.repository.UserRepository;
import com.yan.virtuallibrary.auth.dto.AuthenticationDTO;
import com.yan.virtuallibrary.auth.dto.LoginResponseDTO;
import com.yan.virtuallibrary.auth.dto.RegisterDTO;
import com.yan.virtuallibrary.common.exception.BadRequestException;
import com.yan.virtuallibrary.security.TokenService;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserEntity) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));

    }

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
                registerDTO.role()
        );
        this.userRepository.save(user);
        return ResponseEntity.status(201).build();
    }
}
