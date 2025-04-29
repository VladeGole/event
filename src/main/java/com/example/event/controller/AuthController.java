package com.example.event.controller;

import com.example.event.dto.auth.AuthenticationRequestDto;
import com.example.event.dto.auth.AuthenticationResponseDto;
import com.example.event.dto.auth.RegisterRequest;
import com.example.event.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentacation")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody RegisterRequest registerRequest){
        AuthenticationResponseDto response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto authenticateRequest){
        AuthenticationResponseDto response = authService.authenticate(authenticateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
