package com.epam.gymcore.dao;

import com.epam.gymcore.dao.storage.InMemoryStorage;
import com.epam.gymcore.domain.model.Trainer;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDaoImpl extends AbstractDao<Trainer> {

    public TrainerDaoImpl(InMemoryStorage<Trainer> storage) {
        super(storage);
    }

}
