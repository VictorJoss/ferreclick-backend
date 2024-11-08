package com.victordev.ferreclickbackend.dto.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationBody {

    @NotBlank private String name;
    @NotBlank private String username;
    @NotBlank private String email;
    @NotBlank private String password;
    @NotBlank private String role;
}
