package com.epam.gymcore.dao;

import com.epam.gymcore.dao.api.CommonDao;
import com.epam.gymcore.dao.storage.InMemoryStorage;
import com.epam.gymcore.domain.model.Identifiable;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public abstract class AbstractDao<E extends Identifiable<Long>> implements CommonDao<E,Long> {
    protected InMemoryStorage<E> storage;

    @Override
    public List<E> findAll(){
        return storage.getStorage().values().stream().toList();
    }

    @Override
    public Optional<E> findById(Long aLong) {
        return Optional.ofNullable(storage.getStorage().get(aLong));
    }

    @Override
    public void remove(Long aLong) {
        storage.getStorage().remove(aLong);
    }

    @Override
    public void save(E e) {
        if (e.getId() == null) {
            Long nextId = storage.getNextIndex();
            e.setId(nextId);
        }
        storage.getStorage().put(e.getId(), e);
    }
}
