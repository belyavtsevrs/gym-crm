package com.epam.gymcore.domain.entity;

import com.epam.gymcore.domain.model.User;
import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserImpl implements User {
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String password;
    protected boolean isActive;
}