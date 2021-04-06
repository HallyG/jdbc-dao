package io.github.hallyg.dao;

import io.github.hallyg.dao.exception.DAOException;
import io.github.hallyg.db.Database;
import io.github.hallyg.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDAOImpl implements UserDAO {
  private static final Logger log = LogManager.getLogger(UserDAOImpl.class);

  private static final String FIND_ONE_BY_ID = "select * from users where user_id=?";
  private static final String FIND_ALL = "select * from users";

  private Database database;

  public UserDAOImpl(Database database) {
    this.database = database;
  }

  @Override
  public Optional<User> findOneById(Long id) throws DAOException {
    User user = null;

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_ONE_BY_ID)) {
      statement.setLong(1, id);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          user = createUserFromResultSet(resultSet);
        }
      }
    } catch (SQLException e) {
      throw new DAOException(e);
    }

    return Optional.ofNullable(user);
  }

  @Override
  public List<User> findAll() throws DAOException {
    List<User> users = new ArrayList<>();

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          users.add(createUserFromResultSet(resultSet));
        }
      }
    } catch (SQLException e) {
      throw new DAOException(e);
    }

    return users;
  }

  private User createUserFromResultSet(final ResultSet rs) throws SQLException {
    final User user = new User();
    user.setId(rs.getLong("user_id"));
    user.setEmail(rs.getString("email"));

    return user;
  }
}
