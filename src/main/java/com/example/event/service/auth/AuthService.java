package com.example.event.service.auth;

import com.example.event.core.config.JwtService;
import com.example.event.dto.auth.AuthenticationRequestDto;
import com.example.event.dto.auth.AuthenticationResponseDto;
import com.example.event.dto.auth.RegisterRequest;
import com.example.event.mapper.AuthMapper;
import com.example.event.model.User;
import com.example.event.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(AuthMapper authMapper, PasswordEncoder passwordEncoder, JwtService jwtService, UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponseDto register(RegisterRequest registerRequest) {

        User user= authMapper.fromRegister(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);

        String token=jwtService.generateToken(user);

        return AuthenticationResponseDto.builder().token(token).build();
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticateRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticateRequest.getEmail(),
                        authenticateRequest.getPassword()
                )
        );

        User user=userRepository.findByEmail(authenticateRequest.getEmail()).orElseThrow(()->new UsernameNotFoundException("User not exist"));

        String token=jwtService.generateToken(user);
        return AuthenticationResponseDto.builder().token(token).build();
    }
}
