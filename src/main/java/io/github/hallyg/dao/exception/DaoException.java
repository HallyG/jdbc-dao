package io.github.hallyg.dao.exception;

public class DaoException extends RuntimeException { //PersistentException
  public DaoException(String message) {
    super(message);
  }

  public DaoException(Throwable cause) {
    super(cause);
  }

  public DaoException(String message, Throwable cause) {
    super(message, cause);
  }
}