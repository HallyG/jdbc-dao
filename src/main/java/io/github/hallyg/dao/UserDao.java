package io.github.hallyg.dao;

import io.github.hallyg.dao.exception.DaoException;
import io.github.hallyg.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
  Optional<User> findOneById(Long id) throws DaoException;

  List<User> findAll() throws DaoException;

  void create(User user) throws DaoException;

  void update(User user) throws DaoException;

  void delete(User user) throws DaoException;
}
