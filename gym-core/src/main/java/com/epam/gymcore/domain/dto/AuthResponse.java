package com.epam.gymcore.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String message;

    public AuthResponse(String token) {
        this.token = token;
    }

}
