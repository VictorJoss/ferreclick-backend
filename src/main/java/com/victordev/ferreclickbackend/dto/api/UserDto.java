package com.victordev.ferreclickbackend.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String name;
    private String username;
    private String email;
    private String role;
}