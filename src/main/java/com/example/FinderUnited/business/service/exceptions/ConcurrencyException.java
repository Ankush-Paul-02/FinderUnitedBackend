package com.example.FinderUnited.business.service.exceptions;

public class ConcurrencyException extends RuntimeException {
    public ConcurrencyException(String message) {
        super(message);
    }
}
