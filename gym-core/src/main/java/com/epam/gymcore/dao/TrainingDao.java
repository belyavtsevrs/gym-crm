package com.epam.gymcore.dao;

import com.epam.gymcore.domain.entity.Training;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDao extends AbstractDao<Training,Long> {

    public  TrainingDao(EntityManager entityManager) {
        super(entityManager, Training.class);
    }

}
