package io.github.hallyg;

import io.github.hallyg.dao.UserDao;
import io.github.hallyg.dao.UserDaoDb;
import io.github.hallyg.db.Database;
import io.github.hallyg.db.SQLiteDatabase;
import io.github.hallyg.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
  private static final Logger log = LogManager.getLogger(App.class);
  private static final String DB_URL = "jdbc:sqlite:data/users.db";

  public static void main(String[] args) {
    Database database = createDatabase();

    try {
      createSchema(database);
    } catch (SQLException ex) {
    }

    UserDao userDao = new UserDaoDb(database);
    List<User> users = userDao.findAll();
    log.info(users);
  }

  private static Database createDatabase() {
    return new SQLiteDatabase(DB_URL);
  }

  private static void createSchema(Database database) throws SQLException {
    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("DROP TABLE users")) {
      statement.execute();
    }

    try (Connection connection = database.getConnection();
        PreparedStatement statement =
            connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS users(user_id integer primary key, email text not null unique)")) {
      statement.execute();
    }

    try (Connection connection = database.getConnection();
        PreparedStatement statement =
            connection.prepareStatement("INSERT INTO users (email) VALUES (?)")) {
      statement.setString(1, "test@email.com");
      statement.execute();
    }
  }
}
