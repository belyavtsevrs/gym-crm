package com.epam.gymcore.service.api;

public interface Deleter<ID> {
    void deleteById(ID id);
}
