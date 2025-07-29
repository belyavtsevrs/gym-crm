package com.epam.gymcore.dao;

import com.epam.gymcore.domain.entity.Trainee;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;


@Repository
public class TraineeDao extends AbstractUserDao<Trainee> {

    public TraineeDao(EntityManager entityManager) {
        super(entityManager, Trainee.class);
    }

}
