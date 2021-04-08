package io.github.hallyg.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {
  Connection getConnection() throws SQLException;
  Connection getConnection(String username, String password) throws SQLException;
}
