package com.epam.gymcore.dao;

import com.epam.gymcore.dao.api.CommonDao;
import com.epam.gymcore.domain.entity.AbstractEntity;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;


public abstract class AbstractDao<E extends AbstractEntity, ID> implements CommonDao<E, ID> {
    protected final EntityManager entityManager;
    protected final Class<E> entityType;

    protected AbstractDao(EntityManager entityManager, Class<E> entityType) {
        this.entityManager = entityManager;
        this.entityType = entityType;
    }

    @Override
    public List<E> findAll() {
        Session session = entityManager.unwrap(Session.class);
        try {
            return session.createQuery("FROM " + entityType.getSimpleName(),entityType).getResultList();
        }catch (Exception e){
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Optional<E> findById(ID aID) {
        Session session = entityManager.unwrap(Session.class);
        try {
            return Optional.ofNullable(session.get(entityType, aID));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public E save(E e) {
        Session session = entityManager.unwrap(Session.class);
        try {
            session.persist(e);
            session.flush();
            return e;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to save entity: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void remove(ID aID) {
        Session session = entityManager.unwrap(Session.class);
        try {
            E entity = session.get(entityType, aID);
            if (entity != null) {
                session.remove(entity);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public E update(E e) {
        Session session = entityManager.unwrap(Session.class);
        try {
            session.merge(e);
            session.flush();
            return e;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to save entity: " + ex.getMessage(), ex);
        }
    }
}

