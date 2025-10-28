package com.epam.gymsecurity.service.impl;

import com.epam.gymsecurity.domain.dto.RegisterRequest;
import com.epam.gymsecurity.domain.entity.User;
import com.epam.gymsecurity.repostiory.UserRepository;
import com.epam.gymsecurity.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    @SendTo("")
    @JmsListener(destination = "registration.queue")
    public Long register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username()))
            throw new IllegalStateException("username taken");
        User u = new User();
        u.setUsername(request.username());
        u.setPassword(encoder.encode(request.password()));
        u.setRole(request.role());
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
