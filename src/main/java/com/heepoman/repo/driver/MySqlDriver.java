package com.heepoman.repo.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlDriver implements RepositoryDriver {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final String url;
  private final String dbName;
  private final String userName;
  private final String password;

  public MySqlDriver() {
    final Properties props  = new Properties();
    try {
      final String configFilePath = "/config.properties";
      InputStream inputStream = this.getClass().getResourceAsStream(configFilePath);
      props.load(inputStream);
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException ex) {
      logger.error(ex.getMessage());
    } catch (IOException ex) {
      logger.error(ex.getMessage());
    }
    this.url = props.getProperty("mysql.host");
    this.dbName = props.getProperty("mysql.dbname");
    this.userName = props.getProperty("mysql.username");
    this.password = props.getProperty("mysql.password");
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url + "/" + dbName, userName, password);
  }

}