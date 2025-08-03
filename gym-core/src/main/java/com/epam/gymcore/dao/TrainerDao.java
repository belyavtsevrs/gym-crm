package com.epam.gymcore.dao;

import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.domain.entity.Trainer;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDao extends AbstractUserDao<Trainer>{

    public  TrainerDao(EntityManager entityManager) {
        super(entityManager, Trainer.class);
    }

}
