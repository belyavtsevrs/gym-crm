package com.epam.gymsecurity.service;

import com.epam.gymsecurity.domain.dto.RegisterRequest;
import com.epam.gymsecurity.domain.entity.User;

public interface UserService {
    Long register(RegisterRequest request);
    User requireByUsername(String username);
    boolean matches(String raw, String hash);
}
