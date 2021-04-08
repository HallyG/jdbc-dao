package io.github.hallyg.db;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.SQLiteDataSource;

public class SQLiteDatabase implements Database {
  private static final Logger log = LogManager.getLogger(SQLiteDatabase.class);
  private SQLiteDataSource dataSource;

  public SQLiteDatabase(final String url) {
    dataSource = new SQLiteDataSource();
    dataSource.setUrl(url);
  }

  @Override
  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return dataSource.getConnection(username, password);
  }
}
