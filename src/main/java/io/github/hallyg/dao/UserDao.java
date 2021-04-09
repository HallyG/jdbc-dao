package io.github.hallyg.dao;

import io.github.hallyg.dao.exception.PersistentException;
import io.github.hallyg.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
  Optional<User> findOneById(Long id) throws PersistentException;

  List<User> findAll() throws PersistentException;

  void create(User user) throws PersistentException;

  void update(User user) throws PersistentException;

  void delete(User user) throws PersistentException;
}
