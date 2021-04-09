package io.github.hallyg.dao;

import io.github.hallyg.dao.exception.PersistentException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<T, PK extends Serializable> {
  Optional<T> findOneById(PK id) throws PersistentException;

  List<T> findAll() throws PersistentException;

  void create(T entity) throws PersistentException;

  void update(T entity) throws PersistentException;

  void delete(T entity) throws PersistentException;
}
