package com.heepoman.repo.pool;

import com.heepoman.repo.driver.MySqlDriver;
import com.heepoman.repo.driver.RepositoryDriver;
import com.heepoman.repo.exception.ConnectionPoolException;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class ConnectionPoolTest {

  RepositoryDriver mysqlDriver = new MySqlDriver();
  ConnectionPool pool = new ConnectionPoolImpl.ConnectionPoolBuilder(mysqlDriver).build();

  @Test
  public void getConnection() throws ConnectionPoolException, SQLException {
    assertTrue(pool.getConnectionFromPool().isValid(1));
  }

}
