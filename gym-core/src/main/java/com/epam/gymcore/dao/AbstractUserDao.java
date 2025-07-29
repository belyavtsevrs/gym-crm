package com.epam.gymcore.dao;

import com.epam.gymcore.dao.api.UserDao;
import com.epam.gymcore.domain.entity.User;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public abstract class AbstractUserDao<E extends User> extends AbstractDao<E,Long> implements UserDao<E> {

    protected AbstractUserDao(EntityManager entityManager, Class<E> entityType) {
        super(entityManager, entityType);

    }

    @Override
    public Optional<E> findByUsername(String username) {
        Session session = entityManager.unwrap(Session.class);
        try {
            E user = session.createQuery("SELECT u FROM " + entityType.getSimpleName() + " u WHERE u.username = :username ", entityType)
                    .setParameter("username", username)
                    .uniqueResult();

            return Optional.ofNullable(user);
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<E> findByUsernameAndPassword(String username, String password) {
         Session session = entityManager.unwrap(Session.class);
         try {
             E user = session.createQuery(
                     "SELECT u FROM " +entityType.getSimpleName() + " u WHERE u.username = :username AND u.password = :password ", entityType)
                     .setParameter("username", username)
                     .setParameter("password", password)
                     .uniqueResult();

             return Optional.ofNullable(user);
         }catch (Exception e){
             e.printStackTrace();
             return Optional.empty();
         }
    }

    @Override
    public void updatePasswordByUsername(String username, String newPassword) {
        Session session = entityManager.unwrap(Session.class);
            session.createQuery("UPDATE " + entityType.getSimpleName() +
                            " u SET u.password = :password WHERE u.username = :username")
                    .setParameter("password", newPassword)
                    .setParameter("username", username)
                    .executeUpdate();

    }

    @Override
    public void setActiveStatusByUsername(String username, boolean isActive) {
        Session session = entityManager.unwrap(Session.class);
            session.createQuery(
                    "UPDATE " + entityType.getSimpleName() + " u SET u.isActive  = :isActive WHERE u.username = :username " )
                    .setParameter("isActive",isActive)
                    .setParameter("username",username)
                    .executeUpdate();
    }

    @Override
    public void deleteByUsername(String username) {
        Session session = entityManager.unwrap(Session.class);
            session.createQuery("DELETE FROM " + entityType.getSimpleName() +
                            " u WHERE u.username = :username",entityType)
                    .setParameter("username", username)
                    .executeUpdate();

    }

    @Override
    public List<E> findAllActive() {
        Session session = entityManager.unwrap(Session.class);
        try{
            return session.createQuery("FROM "+ entityType.getSimpleName(),entityType)
                    .getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return List.of();
    }
}
