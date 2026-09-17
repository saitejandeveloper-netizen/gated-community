package com.gatedcommunity.gated_community.service;

import com.gatedcommunity.gated_community.dto.*;
import com.gatedcommunity.gated_community.entity.Role;
import com.gatedcommunity.gated_community.entity.User;
import com.gatedcommunity.gated_community.repository.UserRepository;
import com.gatedcommunity.gated_community.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email()))
            throw new IllegalArgumentException("Email already registered");
        if (userRepository.existsByPhone(req.phone()))
            throw new IllegalArgumentException("Phone already registered");

        User user = User.builder()
                .fullName(req.fullName())
                .email(req.email())
                .phone(req.phone())
                .password(passwordEncoder.encode(req.password()))
                .flatNumber(req.flatNumber())
                .role(Role.RESIDENT)
                .enabled(true)
                .build();

        userRepository.save(user);
        return buildResponse(user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password()));

        User user = userRepository.findByEmail(req.email()).orElseThrow();
        return buildResponse(user.getEmail(), user.getRole().name());
    }

    private AuthResponse buildResponse(String email, String role) {
        String token = jwtService.generateToken(userDetailsService.loadUserByUsername(email));
        return new AuthResponse(token, email, role);
    }
}