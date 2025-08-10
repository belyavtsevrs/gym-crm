package com.epam.gymcore.service.impl;

import com.epam.gymcore.dao.AbstractUserDao;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.domain.entity.User;
import com.epam.gymcore.service.UserService;
import com.epam.gymcore.service.api.Creator;
import com.epam.gymcore.service.api.Deleter;
import com.epam.gymcore.service.api.Retriever;
import com.epam.gymcore.service.api.Updater;
import com.epam.gymcore.util.UserUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractUserService<E extends User> implements UserService<E>, Creator<E>, Retriever<E, Long>, Updater<E>, Deleter<Long> {
    protected final AbstractUserDao<E> dao;

    protected AbstractUserService(AbstractUserDao<E> dao) {
        this.dao = dao;
    }

    @Override
    public E create(E entity) {
        String generatedUsername = UserUtil.createUsername(entity, username ->
                dao.findByUsername(username).isPresent()
        );

        String generatedPassword = UserUtil.generatePassword();

        entity.setUsername(generatedUsername);
        entity.setPassword(generatedPassword);

        log.info("(AbstractUserService) entity before save: = {}",entity);
        return dao.save(entity);
    }

    @Override
    public void deleteById(Long aLong) {
        dao.remove(aLong);
    }

    @Override
    public List<E> findAll() {
        return dao.findAll();
    }

    @Override
    public Optional<E> findById(Long aLong) {
        return dao.findById(aLong);
    }

    @Override
    public E update(E entity) {
        return dao.update(entity);
    }

    @Override
    public Optional<E> findByUsername(String username) {
        return dao.findByUsername(username);
    }

    @Override
    public Optional<E> findByUsernameAndPassword(String username, String password) {
        return dao.findByUsernameAndPassword(username,password);
    }

    @Override
    public void updatePasswordByUsername(String username, String newPassword) {
        dao.updatePasswordByUsername(username,newPassword);
    }

    @Override
    public void setActiveStatusByUsername(String username, boolean isActive) {
        dao.setActiveStatusByUsername(username,isActive);
    }

    @Override
    public void deleteByUsername(String username) {
        dao.deleteByUsername(username);
    }

    @Override
    public List<E> findAllActive() {
        return dao.findAllActive();
    }

    @Override
    public List<Training> getUsersByUsernameAndCriteria(String username, LocalDateTime from, LocalDateTime to) {
        return dao.getUsersByUsernameAndCriteria(username,from,to);
    }
}
