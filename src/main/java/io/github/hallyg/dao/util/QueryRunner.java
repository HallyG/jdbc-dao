package io.github.hallyg.dao.util;

import io.github.hallyg.db.Database;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryRunner {
  private final Database database;

  public QueryRunner(Database database) {
    this.database = database;
  }

  public void populateStatement(final PreparedStatement statement, final Object... params)
      throws SQLException {
    if (params == null) {
      return;
    }

    ParameterMetaData parameterMetaData = statement.getParameterMetaData();
    final int expectedParams = parameterMetaData.getParameterCount();
    final int paramsCount = params == null ? 0 : params.length;

    if (expectedParams != paramsCount) {
      throw new SQLException(
          String.format(
              "Wrong number of parameters: expected %d, given %d.", expectedParams, paramsCount));
    }

    for (int i = 0; i < params.length; i++) {
      if (params[i] != null) {
        statement.setObject(i + 1, params[i]);
      }
    }
  }

  public int update(final String sql, final Object... params) throws SQLException {
    if (sql == null) {
      throw new SQLException("Null SQL statement.");
    }

    int rows;
    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      populateStatement(statement, params);
      rows = statement.executeUpdate();
    }

    return rows;
  }

  public <T> T insert(
      final String sql, final ResultSetHandler<T> resultSetHandler, final Object... params)
      throws SQLException {
    if (sql == null) {
      throw new SQLException("Null SQL statement.");
    }

    if (resultSetHandler == null) {
      throw new SQLException("Null ResultSetHandler.");
    }

    T generatedKeys;

    try (Connection connection = database.getConnection();
        PreparedStatement statement =
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      populateStatement(statement, params);
      statement.executeUpdate();

      try (ResultSet resultSet = statement.getGeneratedKeys()) {
        generatedKeys = resultSetHandler.handle(resultSet);
      }
    }

    return generatedKeys;
  }

  public <T> T query(final String sql, final ResultSetHandler<T> resultSetHandler)
      throws SQLException {
    return query(sql, resultSetHandler, (Object[]) null);
  }

  public <T> T query(
      final String sql, final ResultSetHandler<T> resultSetHandler, final Object... params)
      throws SQLException {
    if (sql == null) {
      throw new SQLException("Null SQL statement.");
    }

    if (resultSetHandler == null) {
      throw new SQLException("Null ResultSetHandler.");
    }

    T result;

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      populateStatement(statement, params);

      try (ResultSet resultSet = statement.executeQuery()) {
        result = resultSetHandler.handle(resultSet);
      }
    }

    return result;
  }
}
