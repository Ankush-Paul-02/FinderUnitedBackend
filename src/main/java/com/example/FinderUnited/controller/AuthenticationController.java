package com.example.FinderUnited.controller;

import com.example.FinderUnited.business.dto.AuthencationRequest;
import com.example.FinderUnited.business.dto.DefaultResponseDto;
import com.example.FinderUnited.business.dto.RegisterRequest;
import com.example.FinderUnited.business.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.FinderUnited.business.dto.DefaultResponseDto.Status.SUCCESS;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<DefaultResponseDto> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        return ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("data", authenticationService.register(registerRequest)),
                        "User registered successfully."
                )
        );
    }

    @PostMapping("/authenticate")
    public ResponseEntity<DefaultResponseDto> authenticate(
            @Valid @RequestBody AuthencationRequest request
    ) {
        return ok(
                new DefaultResponseDto(
                        SUCCESS,
                        Map.of("data", authenticationService.authenticate(request)),
                        "User authenticated successfully"
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<DefaultResponseDto> refresh(@RequestParam("token") String refreshToken) {
        try {
            return ResponseEntity.ok(
                    new DefaultResponseDto(
                            SUCCESS,
                            Map.of("data", authenticationService.refreshToken(refreshToken)),
                            "User authenticated successfully"
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
