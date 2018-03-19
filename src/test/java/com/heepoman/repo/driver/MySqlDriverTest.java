package com.heepoman.repo.driver;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;


public class MySqlDriverTest {

  MySqlDriver driver = new MySqlDriver();

  @Test
  public void getConnection() throws SQLException {
    assertTrue(driver.getConnection().isValid(5));
  }

}
