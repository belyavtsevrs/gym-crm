package com.epam.gymcore.dao.api;

import com.epam.gymcore.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao<E extends User> extends CommonDao<E, Long> {
    Optional<E> findByUsername(String username);

    Optional<E> findByUsernameAndPassword(String username, String password);

    void updatePasswordByUsername(String username, String newPassword);

    void setActiveStatusByUsername(String username, boolean isActive);

    void deleteByUsername(String username);

    List<E> findAllActive();
}
