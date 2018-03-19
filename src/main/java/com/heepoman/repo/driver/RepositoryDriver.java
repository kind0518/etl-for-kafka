package com.heepoman.repo.driver;

import java.sql.Connection;
import java.sql.SQLException;

public interface RepositoryDriver {

  Connection getConnection() throws SQLException;

}
