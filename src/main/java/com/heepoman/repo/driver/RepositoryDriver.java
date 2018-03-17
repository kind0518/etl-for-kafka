package com.heepoman.repo.driver;

import java.sql.Connection;
import java.sql.SQLException;

public interface RepositoryDriver {

  public void init();

  public Connection getConnection() throws SQLException;
}
