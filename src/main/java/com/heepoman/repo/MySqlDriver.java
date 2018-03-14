package com.heepoman.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlDriver {
  //TODO: connection pool이 필요하다.
  private Connection connection;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public MySqlDriver() {
    init();
  }

  public void init() {
    try {
      final String configFilePath = "/config.properties";
      InputStream inputStream = this.getClass().getResourceAsStream(configFilePath);
      Properties props = new Properties();
      props.load(inputStream);

      String url = props.getProperty("mysql.host");
      String userName = props.getProperty("mysql.username");
      String password = props.getProperty("mysql.password");

      Class.forName("com.mysql.jdbc.Driver");
      this.connection = DriverManager.getConnection(url, userName, password);
    } catch (ClassNotFoundException ex) {
      logger.error(ex.getMessage());
    } catch (SQLException ex) {
      logger.error(ex.getMessage());
    } catch (IOException ex) {
      logger.error(ex.getMessage());
    }
  }

  public Connection getConnection() {
    return this.connection;
  }

  public void close() {
    try {
      this.connection.close();
    } catch (SQLException ex) {
      logger.error(ex.getMessage());
    }
  }

}