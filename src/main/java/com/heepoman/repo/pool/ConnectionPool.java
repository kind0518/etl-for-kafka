package com.heepoman.repo.pool;

import com.heepoman.repo.exception.ConnectionPoolException;

import java.sql.Connection;

public interface ConnectionPool {
  Connection getConnectionFromPool() throws ConnectionPoolException;
}
