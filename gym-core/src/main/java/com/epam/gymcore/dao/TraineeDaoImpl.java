package com.epam.gymcore.dao;

import com.epam.gymcore.dao.storage.InMemoryStorage;
import com.epam.gymcore.domain.model.Trainee;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDaoImpl extends AbstractDao<Trainee> {

    public TraineeDaoImpl(InMemoryStorage<Trainee> storage) {
        super(storage);
    }

}
