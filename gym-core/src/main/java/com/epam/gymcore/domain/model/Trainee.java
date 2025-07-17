package com.epam.gymcore.domain.model;

import java.time.LocalDate;

public interface Trainee extends User,Identifiable<Long> {
    LocalDate getDateBirth();
    String getAddress();
}
