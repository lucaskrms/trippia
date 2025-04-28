package com.trippia.trippia.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String name;
    private String email;

    public LoginResponseDTO(String token, String name, String email) {
        this.token = token;
        this.name = name;
        this.email = email;
    }
}