package com.epam.gymcore.dao;

import com.epam.gymcore.dao.api.UserDao;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.domain.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractUserDao<E extends User> extends AbstractDao<E,Long> implements UserDao<E> {

    protected AbstractUserDao(EntityManager entityManager, Class<E> entityType) {
        super(entityManager, entityType);

    }

    @Override
    public Optional<E> findByUsername(String username) {
        List<E> results = entityManager.createQuery(
                        "SELECT u FROM " + entityType.getSimpleName() + " u WHERE u.username = :username", entityType)
                .setParameter("username", username)
                .getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Optional<E> findByUsernameAndPassword(String username, String password) {
        List<E> results = entityManager.createQuery(
                        "SELECT u FROM " + entityType.getSimpleName() + " u WHERE u.username = :username AND u.password = :password", entityType)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public void updatePasswordByUsername(String username, String newPassword) {
        try {
            Query query = entityManager.createQuery(
                    "UPDATE " + entityType.getSimpleName() + " u SET u.password = :password WHERE u.username = :username");
            query.setParameter("password", newPassword);
            query.setParameter("username", username);
            query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setActiveStatusByUsername(String username, boolean isActive) {
        try {
            Query query = entityManager.createQuery(
                    "UPDATE " + entityType.getSimpleName() + " u SET u.isActive = :isActive WHERE u.username = :username");
            query.setParameter("isActive", isActive);
            query.setParameter("username", username);
            query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByUsername(String username) {
        try {
            Query query = entityManager.createQuery(
                    "DELETE FROM " + entityType.getSimpleName() + " u WHERE u.username = :username");
            query.setParameter("username", username);
            query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public List<E> findAllActive() {
        try {
            return entityManager.createQuery(
                            "SELECT u FROM " + entityType.getSimpleName() + " u WHERE u.isActive = true", entityType)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Training> getUsersByUsernameAndCriteria(String username, LocalDateTime from, LocalDateTime to) {
        String utable = entityType.getSimpleName().toLowerCase();
        String query = "SELECT T FROM "+Training.class.getSimpleName()+" T JOIN T." + utable + " U WHERE U.username = :username";

        List<Training> trainings = entityManager.createQuery(query, Training.class)
                .setParameter("username",username)
                .getResultList();
        log.info("user = {}",trainings.get(0).getTrainee());

        return trainings.stream()
                .filter(x->{
                    return x.getTrainingDate().isBefore(to) && x.getTrainingDate().plus(x.getDuration()).isAfter(from);
                })
                .toList();
    }
}
