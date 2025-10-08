package com.epam.gymsecurity.service;

import com.epam.gymsecurity.domain.entity.User;

public interface UserService {
    Long register(String username,String password,String role);
    User requireByUsername(String username);
    boolean matches(String raw, String hash);
}
