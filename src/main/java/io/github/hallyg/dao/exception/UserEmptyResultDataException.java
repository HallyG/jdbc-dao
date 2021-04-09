package io.github.hallyg.dao.exception;

public class UserEmptyResultDataException extends PersistentException {
  public UserEmptyResultDataException(String message) {
    super(message);
  }

  public UserEmptyResultDataException(Throwable cause) {
    super(cause);
  }

  public UserEmptyResultDataException(String message, Throwable cause) {
    super(message, cause);
  }
}
