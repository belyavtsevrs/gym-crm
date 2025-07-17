package com.epam.gymcore.service.Impl;

import com.epam.gymcore.dao.api.CommonDao;
import com.epam.gymcore.domain.model.Identifiable;
import com.epam.gymcore.service.api.Deleter;
import com.epam.gymcore.service.api.Retriever;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractService<E, D extends CommonDao<E, Long>>
    implements Deleter<Long>, Retriever<E,Long> {
    protected final D dao;

    public AbstractService(D dao) {
        this.dao = dao;
    }

    @Override
    public void deleteById(Long aLong) {
        dao.remove(aLong);
        log.info("Entity with id {} hase been deleted",aLong);
    }

    @Override
    public List<E> findAll() {
        return dao.findAll();
    }

    @Override
    public Optional<E> findById(Long aLong) {
        Optional<E> e = dao.findById(aLong);
        if(e.isPresent()){
            return e;
        }
        return Optional.empty();
    }

}
