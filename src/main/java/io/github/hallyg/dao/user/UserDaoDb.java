package io.github.hallyg.dao.user;

import io.github.hallyg.dao.exception.PersistentException;
import io.github.hallyg.dao.exception.UserEmptyResultDataException;
import io.github.hallyg.dao.exception.UserExistsException;
import io.github.hallyg.dao.exception.UserNotFoundException;
import io.github.hallyg.dao.util.QueryRunner;
import io.github.hallyg.domain.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDaoDb implements UserDao {
  private static final Logger log = LogManager.getLogger(UserDaoDb.class);

  private static final String SQL_FIND_ONE_BY_ID = "select * from users where user_id=?";
  private static final String SQL_FIND_ALL = "select * from users";
  private static final String SQL_INSERT_ONE = "insert into users (email) values (?)";
  private static final String SQL_UPDATE_ONE = "update users set email=? where user_id=?";
  private static final String SQL_DELETE_ONE = "delete from users where user_id=?";

  private QueryRunner queryRunner;

  public UserDaoDb(QueryRunner queryRunner) {
    this.queryRunner = queryRunner;
  }

  @Override
  public Optional<User> findOneById(Long id) throws PersistentException {
    if (id == null) {
      throw new IllegalArgumentException("The given id must not be null.");
    }

    log.debug("Executing SQL query [{}]", SQL_FIND_ONE_BY_ID);

    try {
      User user = queryRunner.query(SQL_FIND_ONE_BY_ID, this::resultSetToUser, id);
      return Optional.ofNullable(user);
    } catch (SQLException ex) {
      throw new PersistentException(ex.getMessage(), ex);
    }
  }

  @Override
  public List<User> findAll() throws PersistentException {
    log.debug("Executing SQL query [{}]", SQL_FIND_ALL);

    try {
      return queryRunner.query(SQL_FIND_ALL, this::resultSetToUserList);
    } catch (SQLException ex) {
      throw new PersistentException(ex.getMessage(), ex);
    }
  }

  @Override
  public void create(User user) throws PersistentException {
    if (user == null) {
      throw new IllegalArgumentException("The given user must not be null.");
    }

    if (findOneById(user.getId()).isPresent()) {
      throw new UserExistsException(String.format("User with id=%s already exists.", user.getId()));
    }

    log.debug("Executing SQL update [{}]", SQL_INSERT_ONE);

    try {
      Long userId = queryRunner.insert(SQL_INSERT_ONE, this::resultSetToId, user.getEmail());
      if (userId != null) {
        user.setId(userId);
      } else {
        throw new UserEmptyResultDataException("");
      }
    } catch (SQLException ex) {
      throw new PersistentException(ex.getMessage(), ex);
    }
  }

  @Override
  public void update(User user) throws PersistentException {
    if (user == null) {
      throw new IllegalArgumentException("The given user must not be null.");
    }

    if (!findOneById(user.getId()).isPresent()) {
      throw new UserNotFoundException(String.format("No user with id=%s exists.", user.getId()));
    }

    log.debug("Executing SQL update [{}]", SQL_UPDATE_ONE);

    try {
      int affectedRows = queryRunner.update(SQL_UPDATE_ONE, user.getEmail(), user.getId());
      log.debug("SQL update affected {} rows", affectedRows);

      if (affectedRows == 0) {
        throw new UserEmptyResultDataException("");
      }
    } catch (SQLException ex) {
      throw new PersistentException(ex.getMessage(), ex);
    }
  }

  @Override
  public void delete(User user) throws PersistentException {
    if (user == null) {
      throw new IllegalArgumentException("The given user must not be null.");
    }

    log.debug("Executing SQL update [{}]", SQL_DELETE_ONE);

    try {
      int affectedRows = queryRunner.update(SQL_DELETE_ONE, user.getId());
      log.debug("SQL update affected {} rows", affectedRows);

      if (affectedRows == 0) {
        throw new UserEmptyResultDataException("");
      }
    } catch (SQLException ex) {
      throw new PersistentException(ex.getMessage(), ex);
    }
  }

  private List<User> resultSetToUserList(final ResultSet rs) throws SQLException {
    List<User> userList = new ArrayList<>();

    while (rs.next()) {
      User user = new User(rs.getLong("user_id"), rs.getString("email"));
      userList.add(user);
    }

    return userList;
  }

  private User resultSetToUser(final ResultSet rs) throws SQLException {
    return rs.next() ? new User(rs.getLong("user_id"), rs.getString("email")) : null;
  }

  private Long resultSetToId(final ResultSet rs) throws SQLException {
    return rs.next() ? rs.getLong(1) : null;
  }
}
