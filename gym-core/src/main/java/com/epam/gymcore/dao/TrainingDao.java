package com.epam.gymcore.dao;

import com.epam.gymcore.domain.entity.Training;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TrainingDao extends AbstractDao<Training,Long> {

    public  TrainingDao(EntityManager entityManager) {
        super(entityManager, Training.class);
    }

    public List<Training> getTrainingByTrainee(Long traineeId){
        return entityManager.createQuery(
                        "SELECT T FROM Training T WHERE T.trainee.id = :traineeId", Training.class)
                .setParameter("traineeId", traineeId)
                .getResultList();
    };

    public List<Training> getTrainingByTrainer(Long trainerId){
        return entityManager.createQuery(
                        "SELECT T FROM Training T WHERE T.trainer.id = :trainerId", Training.class)
                .setParameter("trainerId", trainerId)
                .getResultList();
    };

    public Optional<Training> findTrainingByTrainerAndDate(String trainerUsername, LocalDateTime date){
        Training training = entityManager.createQuery(
                        "SELECT T FROM Training T WHERE T.trainer.username = :username AND T.trainingDate = :trainingDate",
                        Training.class)
                .setParameter("username", trainerUsername)
                .setParameter("trainingDate", date)
                .getSingleResult();
         return Optional.of(training);
    }

}
