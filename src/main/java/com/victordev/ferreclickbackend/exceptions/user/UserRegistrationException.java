package com.victordev.ferreclickbackend.exceptions.user;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String message, Throwable e) {
        super(message, e);
    }
}
