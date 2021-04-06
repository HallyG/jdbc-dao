package io.github.hallyg.dao;

import io.github.hallyg.dao.exception.DAOException;
import io.github.hallyg.domain.User;
import java.util.Optional;

public interface UserDAO {
  Optional<User> findOneById(Long id) throws DAOException;
}
