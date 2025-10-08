package com.epam.gymsecurity.service.impl;

import com.epam.gymsecurity.domain.entity.User;
import com.epam.gymsecurity.repostiory.UserRepository;
import com.epam.gymsecurity.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public Long register(String username, String password, String role) {
        if (userRepository.existsByUsername(username))
            throw new IllegalStateException("username taken");
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setRole(role);
        log.info("user = {}",u);
        return userRepository.save(u).getId();
    }

    public User requireByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public boolean matches(String raw, String hash) {
        return encoder.matches(raw, hash);
    }
}
