package com.epam.gymsecurity.domain.dto;

public record RegisterRequest(
        String username,
        String password,
        String role
) {}
