package com.example.FinderUnited.business.service.impl;

import com.example.FinderUnited.business.dto.AuthencationRequest;
import com.example.FinderUnited.business.dto.AuthenticationResponse;
import com.example.FinderUnited.business.dto.RegisterRequest;
import com.example.FinderUnited.business.service.AuthenticationService;
import com.example.FinderUnited.business.service.exceptions.UserInfoException;
import com.example.FinderUnited.data.entities.User;
import com.example.FinderUnited.data.enums.Role;
import com.example.FinderUnited.data.repositories.UserRepository;
import com.example.FinderUnited.security.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {

        Optional<User> optionalUser = userRepository.findByEmail(registerRequest.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserInfoException("User is already registered with email " + registerRequest.getEmail());
        }

        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        user = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefresh(new HashMap<>(), user);
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthencationRequest authencationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authencationRequest.getEmail(),
                            authencationRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new UserInfoException("Invalid email or password");
        }

        Optional<User> optionalUser = userRepository.findByEmail(authencationRequest.getEmail());
        if (optionalUser.isEmpty()) {
            throw new JwtException("User not found with email " + authencationRequest.getEmail());
        }
        User user = optionalUser.get();
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefresh(new HashMap<>(), user);
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        Optional<User> optionalUser = userRepository.findByEmail(jwtService.getEmailFromToken(refreshToken));
        if (optionalUser.isEmpty()) {
            throw new JwtException("User not found with email " + jwtService.getEmailFromToken(refreshToken));
        }
        User user = optionalUser.get();
        String jwtToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefresh(new HashMap<>(), user);
        return AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public Boolean isValidToken(String token) {
        return jwtService.validateToken(token);
    }
}
