package com.epam.gymcore.service.api;

public interface Updater<E> {
    E update(E entity);
}
