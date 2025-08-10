package com.epam.gymcore.dao;

import com.epam.gymcore.domain.entity.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainingTypeDao extends AbstractDao<TrainingType, Long> {

    public TrainingTypeDao(EntityManager entityManager) {
        super(entityManager, TrainingType.class);
    }

    public Optional<TrainingType> findByName(String type){
        try {
            TrainingType trainingType = entityManager
                    .createQuery("SELECT T FROM TrainingType T WHERE T.name = :type", TrainingType.class)
                    .setParameter("type", type.toUpperCase())
                    .getSingleResult();


            return Optional.of(trainingType);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


}
