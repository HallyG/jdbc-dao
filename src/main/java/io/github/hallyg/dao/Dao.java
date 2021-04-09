package io.github.hallyg.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<T, PK extends Serializable> {
    Optional<T> findOneById(PK id);

    List<T> findAll();

    void create(T entity);

    void update(T entity);

    void delete(T entity);
}
