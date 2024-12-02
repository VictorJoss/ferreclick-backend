package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.RegistrationBody;
import com.victordev.ferreclickbackend.dto.api.UserDto;
import com.victordev.ferreclickbackend.dto.security.LoginRequest;
import com.victordev.ferreclickbackend.dto.security.LoginResponse;
import com.victordev.ferreclickbackend.exceptions.user.UserAlreadyExistsException;
import com.victordev.ferreclickbackend.persistence.entity.User;

public interface IUserService {
    User registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException;
    LoginResponse loginUser(LoginRequest loginRequest);
    void logout();
    UserDto getUser(Long userId);
}
