package com.victordev.ferreclickbackend.exceptions.security;

public class AuthUserException extends RuntimeException {
    public AuthUserException(String message) {
        super(message);
    }
}
