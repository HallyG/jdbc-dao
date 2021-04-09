package io.github.hallyg.dao.exception;

public class PersistentException extends RuntimeException {
  public PersistentException(String message) {
    super(message);
  }

  public PersistentException(Throwable cause) {
    super(cause);
  }

  public PersistentException(String message, Throwable cause) {
    super(message, cause);
  }
}
