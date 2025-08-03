package com.epam.gymcore.dao;

import com.epam.gymcore.domain.entity.TrainingType;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainingTypeDto extends AbstractDao<TrainingType, Long> {

    public TrainingTypeDto(EntityManager entityManager) {
        super(entityManager, TrainingType.class);
    }

    public Optional<TrainingType> findByTitle(String type){
        Session session = entityManager.unwrap(Session.class);
        try {
            TrainingType trainingType =
                    session.createQuery("SELECT t FROM " + entityType.getSimpleName() + " t WHERE t.name = :type", TrainingType.class)
                            .setParameter("type", type)
                            .uniqueResult();

            return Optional.ofNullable(trainingType);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
