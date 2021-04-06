package io.github.hallyg.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {
  abstract Connection getConnection() throws SQLException;
}
