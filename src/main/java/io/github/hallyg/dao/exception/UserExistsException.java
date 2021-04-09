package io.github.hallyg.dao.exception;

public class UserExistsException extends PersistentException {
  public UserExistsException(String message) {
    super(message);
  }

  public UserExistsException(Throwable cause) {
    super(cause);
  }

  public UserExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
