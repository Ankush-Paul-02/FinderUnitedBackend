package com.example.FinderUnited.business.service;

import com.example.FinderUnited.business.dto.AuthencationRequest;
import com.example.FinderUnited.business.dto.AuthenticationResponse;
import com.example.FinderUnited.business.dto.RegisterRequest;
import com.example.FinderUnited.data.entities.User;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse authenticate(AuthencationRequest authencationRequest);

    AuthenticationResponse refreshToken(String refreshToken);

    Boolean isValidToken(String token);

    User getAuthenticatedUser();
}
