package com.epam.gymsecurity.controller;

import com.epam.gymsecurity.component.JwtIssuer;
import com.epam.gymsecurity.domain.dto.LoginRequest;
import com.epam.gymsecurity.domain.dto.RegisterRequest;
import com.epam.gymsecurity.domain.entity.User;
import com.epam.gymsecurity.service.UserService;
import com.epam.gymsecurity.service.impl.LoginAttemptsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final LoginAttemptsService attempts;
    private final JwtIssuer jwt;

    @PostMapping("/register")
    public Map<String, Object> createUser(@RequestBody RegisterRequest r) {
        Long userId = userService.register(new RegisterRequest(r.username(),r.password(),r.role()));
        return Map.of("userId", userId);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest r) throws Exception {
        User u = userService.requireByUsername(r.username());

        if (attempts.isLocked(r.username())) {
            throw new ResponseStatusException(HttpStatus.LOCKED, "Account locked");
        }

        if (!userService.matches(r.password(), u.getPassword())) {
            attempts.loginFailed(r.username());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        attempts.loginSucceeded(r.username());
        String token = jwt.issue(u);
        return Map.of("access_token", token, "token_type", "Bearer", "expires_in", 900);
    }

}
