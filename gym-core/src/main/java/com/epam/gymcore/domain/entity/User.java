package com.epam.gymcore.domain.entity;

import com.epam.gymcore.util.UserUtil;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User extends AbstractEntity {
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String password;
    protected boolean isActive;

    public User(String firstName, String lastName, String username, String password, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public User(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    @PrePersist
    private void init(){
        isActive = true;
        if (password == null || password.isBlank()) {
            password = UserUtil.generatePassword();
        }

        if (username == null || username.isBlank()) {
            username = firstName + "." + lastName;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return isActive == user.isActive
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName)
                && Objects.equals(username, user.username)
                && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, username, password, isActive);
    }
}