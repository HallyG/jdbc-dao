package io.github.hallyg.dao.exception;

public class UserNotFoundException extends DaoException {
  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(Throwable cause) {
    super(cause);
  }

  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
