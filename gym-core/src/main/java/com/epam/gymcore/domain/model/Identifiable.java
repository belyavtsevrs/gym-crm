package com.epam.gymcore.domain.model;

public interface Identifiable<ID> {
    ID getId();
    void setId(ID id);
}
