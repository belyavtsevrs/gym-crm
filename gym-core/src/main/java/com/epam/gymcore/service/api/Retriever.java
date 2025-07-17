package com.epam.gymcore.service.api;

import java.util.List;
import java.util.Optional;

public interface Retriever<E,ID> {
    List<E> findAll();
    Optional<E> findById(ID id);
}
