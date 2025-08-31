package com.epam.gymcore.dao;

import com.epam.gymcore.domain.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends AbstractUserDao<User> {

    public UserDao(EntityManager entityManager) {
        super(entityManager, User.class);
    }
}
