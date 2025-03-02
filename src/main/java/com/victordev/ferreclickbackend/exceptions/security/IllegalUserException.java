package com.victordev.ferreclickbackend.exceptions.security;

public class IllegalUserException extends RuntimeException {
    public IllegalUserException(String message) {
        super(message);
    }
}
