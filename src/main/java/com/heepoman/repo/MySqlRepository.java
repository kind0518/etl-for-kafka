package com.heepoman.repo;

import com.heepoman.repo.driver.MySqlDriver;
import com.heepoman.repo.exception.ConnectionPoolException;
import com.heepoman.repo.pool.ConnectionPool;
import com.heepoman.repo.pool.ConnectionPoolImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class MySqlRepository {

  ConnectionPool db;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public MySqlRepository() {
    this.db = new ConnectionPoolImpl.ConnectionPoolBuilder(new MySqlDriver()).build();
  }

  public CompletableFuture<Void> createEventData(
          Long eventId,
          String eventTimestamp,
          Optional<String> serviceCodeOpt,
          Optional<String> eventContext) {
    return CompletableFuture.runAsync(() -> {
      synchronized (this) {
        Connection dbConnection = null;
        PreparedStatement stmt = null;
        try {
          dbConnection = db.getConnectionFromPool();
          stmt = dbConnection.prepareStatement("INSERT INTO event(event_id, event_timestamp, service_code, event_context) VALUES (?, ?, ?, ?)");
          stmt.setLong(1, eventId);
          stmt.setString(2, eventTimestamp);
          stmt.setString(3, serviceCodeOpt.orElse(null));
          stmt.setString(4, eventContext.orElse(null));
          stmt.executeUpdate();
        } catch (SQLException ex) {
          logger.error(ex.getMessage());
        } catch (ConnectionPoolException ex) {
          logger.error(ex.getMessage());
        } finally {
          if(dbConnection != null) {
            try {
              dbConnection.close();
            } catch (SQLException ex) {
              logger.error(ex.getMessage());
            }
          }
          if(stmt != null) {
            try {
              stmt.close();
            } catch (SQLException ex) {
              logger.error(ex.getMessage());
            }
          }
        }
      }
    });
  }

  public CompletableFuture<ResultSet> getEventData(Long eventId) throws SQLException, ConnectionPoolException {
    return CompletableFuture.supplyAsync(() -> {
      try {
        Connection dbConnection = db.getConnectionFromPool();
        PreparedStatement stmt = dbConnection.prepareStatement("SELECT * FROM event WHERE event_id = ?");
        stmt.setLong(1, eventId);
        return stmt.executeQuery();
      } catch (SQLException ex) {
        throw new CompletionException(ex);
      } catch (ConnectionPoolException ex) {
        logger.error(ex.getMessage());
        return null;
      }
    });
  }
}
