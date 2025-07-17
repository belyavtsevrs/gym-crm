package com.epam.gymcore.dao.api;

import java.util.List;
import java.util.Optional;

public interface CommonDao<E,ID> {
    List<E> findAll();
    Optional<E> findById(ID id);
    void save(E e);
    void remove(ID id);
}
