package io.github.hallyg.dao;

import io.github.hallyg.dao.exception.DaoException;
import io.github.hallyg.dao.exception.UserEmptyResultDataException;
import io.github.hallyg.dao.exception.UserExistsException;
import io.github.hallyg.dao.exception.UserNotFoundException;
import io.github.hallyg.db.Database;
import io.github.hallyg.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDaoDb implements UserDao {
  private static final Logger log = LogManager.getLogger(UserDaoDb.class);

  private static final String FIND_ONE_BY_ID = "select * from users where user_id=?";
  private static final String FIND_ALL = "select * from users";
  private static final String INSERT_ONE = "insert into users (email) values (?)";
  private static final String UPDATE_ONE = "update users set email=? where user_id=?";
  private static final String DELETE_ONE = "delete from users where user_id=?";

  private Database database;

  public UserDaoDb(Database database) {
    this.database = database;
  }

  @Override
  public Optional<User> findOneById(Long id) throws DaoException {
    if (id == null) {
      throw new IllegalArgumentException("The given id must not be null.");
    }

    log.debug("Executing SQL query [{}]", FIND_ONE_BY_ID);

    User user = null;
    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_ONE_BY_ID)) {
      statement.setLong(1, id);

      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          user = mapRow(rs);
        }
      }
    } catch (SQLException ex) {
      throw new DaoException(ex.getMessage(), ex);
    }

    return Optional.ofNullable(user);
  }

  @Override
  public List<User> findAll() throws DaoException {
    log.debug("Executing SQL query [{}]", FIND_ALL);

    List<User> users = new ArrayList<>();
    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {

      try (ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {
          users.add(mapRow(rs));
        }
      }
    } catch (SQLException ex) {
      throw new DaoException(ex.getMessage(), ex);
    }

    return users;
  }

  @Override
  public void create(User user) throws DaoException {
    if (user == null) {
      throw new IllegalArgumentException("The given user must not be null.");
    }

    if (findOneById(user.getId()).isPresent()) {
      log.warn(String.format("User with id=%s already exists.", user.getId()));
      throw new UserExistsException(String.format("User with id=%s already exists.", user.getId()));
    }

    log.debug("Executing SQL update [{}]", INSERT_ONE);

    try (Connection connection = database.getConnection();
        PreparedStatement statement =
            connection.prepareStatement(INSERT_ONE, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, user.getEmail());

      int affectedRows = statement.executeUpdate();
      log.trace("SQL update affected {} rows", affectedRows);

      if (affectedRows == 0) {
        throw new UserEmptyResultDataException("");
      }

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          user.setId(generatedKeys.getLong(1));
        }
      }
    } catch (SQLException ex) {
      throw new DaoException(ex.getMessage(), ex);
    }
  }

  @Override
  public void update(User user) throws DaoException {
    if (user == null) {
      throw new IllegalArgumentException("The given user must not be null.");
    }

    if (!findOneById(user.getId()).isPresent()) {
      log.warn(String.format("No user with id=%s exists.", user.getId()));
      throw new UserNotFoundException(String.format("No user with id=%s exists.", user.getId()));
    }

    log.debug("Executing SQL update [{}]", UPDATE_ONE);

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(UPDATE_ONE)) {
      statement.setString(1, user.getEmail());
      statement.setLong(2, user.getId());

      int affectedRows = statement.executeUpdate();
      log.trace("SQL update affected {} rows", affectedRows);

      if (affectedRows == 0) {
        throw new UserEmptyResultDataException("");
      }
    } catch (SQLException ex) {
      throw new DaoException(ex.getMessage(), ex);
    }
  }

  @Override
  public void delete(User user) throws DaoException {
    if (user == null) {
      throw new IllegalArgumentException("The given user must not be null.");
    }

    log.debug("Executing SQL update [{}]", DELETE_ONE);

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(DELETE_ONE)) {
      statement.setLong(1, user.getId());

      int affectedRows = statement.executeUpdate();
      log.trace("SQL update affected {} rows", affectedRows);

      if (affectedRows == 0) {
        throw new UserEmptyResultDataException("");
      }
    } catch (SQLException ex) {
      throw new DaoException(ex.getMessage(), ex);
    }
  }

  private User mapRow(ResultSet rs) throws SQLException {
    return new User(rs.getLong("user_id"), rs.getString("email"));
  }
}
