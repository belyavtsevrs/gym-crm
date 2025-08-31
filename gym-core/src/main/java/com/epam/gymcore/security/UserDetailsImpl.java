package com.epam.gymcore.security;

import com.epam.gymcore.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class UserDetailsImpl implements UserDetails {
    private final User user;
    public UserDetailsImpl(User user) { this.user = user; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(user.getRole());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return user.getIsActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Boolean.TRUE.equals(user.getIsLocked());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
}
