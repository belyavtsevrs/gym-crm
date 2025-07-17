package com.epam.gymcore.domain.model;

public interface Trainer extends User,Identifiable<Long>{
    String getSpecialization();
}
