package com.epam.gymsecurity.config;

import com.epam.gymsecurity.domain.entity.User;
import com.epam.gymsecurity.repostiory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaRegisteredClientRepository  implements RegisteredClientRepository {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(RegisteredClient registeredClient) {
        User user = new User();
        user.setUsername(registeredClient.getClientId());
        user.setPassword(passwordEncoder.encode(registeredClient.getClientSecret()));
        user.setRole("CLIENT");
        user.setIsActive(true);
        userRepository.save(user);
    }

    @Override
    public RegisteredClient findById(String id) {
        return userRepository.findById(Long.valueOf(id))
                .map(this::mapToRegisteredClient)
                .orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return userRepository.findByUsername(clientId)
                .map(this::mapToRegisteredClient)
                .orElse(null);
    }

    private RegisteredClient mapToRegisteredClient(User u) {
        return RegisteredClient.withId(u.getId().toString())
                .clientId(u.getUsername())
                .clientSecret(u.getPassword())
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("read")
                .scope("write")
                .build();
    }
}
