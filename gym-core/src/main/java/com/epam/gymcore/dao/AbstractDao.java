package com.epam.gymcore.dao;

import com.epam.gymcore.dao.api.CommonDao;
import com.epam.gymcore.domain.entity.AbstractEntity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractDao<E extends AbstractEntity, ID> implements CommonDao<E, ID> {
    protected final EntityManager entityManager;
    protected final Class<E> entityType;

    protected AbstractDao(EntityManager entityManager, Class<E> entityType) {
        this.entityManager = entityManager;
        this.entityType = entityType;
    }

    @Override
    public List<E> findAll() {
        String sql = "SELECT e FROM " + entityType.getSimpleName() + " e";
        return entityManager.createQuery(sql).getResultList();
    }

    @Override
    public Optional<E> findById(ID Id) {
       return Optional.ofNullable(entityManager.find(entityType, Id));
    }

    @Override
    public  E  save(E e) {
        if (e.getId() == null) {
            entityManager.persist(e);
        } else {
            e = entityManager.merge(e);
        }
        return e;

    }

    @Override
    public void remove(ID aID) {
        E e = entityManager.find(entityType,aID);
        if(e == null){
            entityManager.merge(e);
        }else
            entityManager.remove(e);
    }

    @Override
    public E update(E e) {
        try {
            E merged = entityManager.merge(e);
            return merged;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}

