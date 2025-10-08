package com.epam.gymcore.domain.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    ROLE_TRAINEE,
    ROLE_TRAINER;
    @Override
    public String getAuthority() {
        return name();
    }
}
