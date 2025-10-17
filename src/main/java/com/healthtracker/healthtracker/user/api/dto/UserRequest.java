package com.healthtracker.healthtracker.user.api.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank
    public String username;
    @NotBlank
    public String password;
}
