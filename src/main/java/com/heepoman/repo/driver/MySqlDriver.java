package com.heepoman.repo.driver;

import com.heepoman.repo.driver.RepositoryDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlDriver implements RepositoryDriver {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  String url;
  String userName;
  String password;
  String dbName;

  public MySqlDriver() { //TODO: Config 객체를 넣으면 어떨까?
    init();
  }

  public void init() {
    try {
      final String configFilePath = "/config.properties";
      InputStream inputStream = this.getClass().getResourceAsStream(configFilePath);
      Properties props = new Properties();
      props.load(inputStream);

      this.url = props.getProperty("mysql.host");
      this.dbName = props.getProperty("mysql.dbname");
      this.userName = props.getProperty("mysql.username");
      this.password = props.getProperty("mysql.password");

      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException ex) {
      logger.error(ex.getMessage());
    } catch (IOException ex) {
      logger.error(ex.getMessage());
    }
  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url + "/" + dbName, userName, password);
  }
}