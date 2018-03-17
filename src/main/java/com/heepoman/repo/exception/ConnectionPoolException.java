package com.heepoman.repo.exception;

public class ConnectionPoolException extends Exception {

  public ConnectionPoolException() {
    super();
  }

  public ConnectionPoolException(String message) {
    super(message);
  }

  public ConnectionPoolException(String message, Exception throwable) {
    super(message, throwable);
  }

  public ConnectionPoolException(Exception throwable) {
    super(throwable);
  }

}
