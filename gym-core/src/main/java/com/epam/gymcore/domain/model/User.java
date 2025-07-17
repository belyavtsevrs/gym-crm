package com.epam.gymcore.domain.model;

public interface User {
    String getFirstName();
    String getLastName();
    String getUsername();
    String getPassword();
    boolean isActive();
}
