package com.example.FinderUnited.data.exceptions;

import com.example.FinderUnited.business.dto.DefaultResponseDto;
import com.example.FinderUnited.business.service.exceptions.AuthenticationEntryPointException;
import com.example.FinderUnited.business.service.exceptions.JWTException;
import com.example.FinderUnited.business.service.exceptions.UnauthorizedException;
import com.example.FinderUnited.business.service.exceptions.UserInfoException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;
import java.util.HashMap;
import java.util.Map;

import static com.example.FinderUnited.business.dto.DefaultResponseDto.Status.FAILED;

@ControllerAdvice
public class CustomControllerAdvisor {

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<DefaultResponseDto> handleDateTimeException(DateTimeException ex) {
        DefaultResponseDto response = new DefaultResponseDto(FAILED, ex.getMessage());
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(JWTException.class)
    public ResponseEntity<DefaultResponseDto> handleJWTException(JWTException ex) {
        DefaultResponseDto response = new DefaultResponseDto(FAILED, ex.getMessage());
        return ResponseEntity.status(403).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<DefaultResponseDto> handleUnauthorizedException(UnauthorizedException ex) {
        DefaultResponseDto response = new DefaultResponseDto(FAILED, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    public ResponseEntity<DefaultResponseDto> handleAuthenticationEntryPointException(AuthenticationEntryPointException ex) {
        DefaultResponseDto response = new DefaultResponseDto(FAILED, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserInfoException.class)
    public ResponseEntity<DefaultResponseDto> handleNewUserException(UserInfoException ex) {
        DefaultResponseDto response = new DefaultResponseDto(FAILED, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultResponseDto> handleInvalidArgumentException(MethodArgumentNotValidException exception) {
        Map<String, String> response = new HashMap<>();
        StringBuilder responseMessage = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldName = ((FieldError) objectError).getField();
            String message = objectError.getDefaultMessage();
            responseMessage.append(fieldName).append(" ").append(message).append(", ");
            response.put(fieldName, message);
        });
        return ResponseEntity.status(500).body(new DefaultResponseDto(FAILED, response, responseMessage.toString()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DefaultResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        return ResponseEntity.status(500).body(new DefaultResponseDto(FAILED, response, ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DefaultResponseDto> handleIllegalArgumentException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(500).body(new DefaultResponseDto(FAILED, ex.getMessage()));
    }
}
