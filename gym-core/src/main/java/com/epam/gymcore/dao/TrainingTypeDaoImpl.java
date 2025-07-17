package com.epam.gymcore.dao;

import com.epam.gymcore.dao.storage.InMemoryStorage;
import com.epam.gymcore.domain.model.TrainingType;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeDaoImpl extends AbstractDao<TrainingType>{

    public TrainingTypeDaoImpl(InMemoryStorage<TrainingType> storage) {
        super(storage);
    }

}
