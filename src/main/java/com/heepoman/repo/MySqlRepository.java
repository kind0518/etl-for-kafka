package com.heepoman.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MySqlRepository {

  private Connection conn;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public MySqlRepository() {
    this.conn = new MySqlDriver().getConnection();
  }

  private String createEventQuery(
          Long eventId,
          String eventTimestamp,
          Optional<String> serviceCodeOpt,
          Optional<String> eventContext) {
    return String.format(
            "INSERT INTO event(event_id, event_timestamp, service_code, event_context) VALUES (%d, %s, %s, %s)",
            eventId,
            eventTimestamp,
            serviceCodeOpt,
            eventContext
    );
  }

  public CompletableFuture<Void> createEventData(
          Long eventId,
          String eventTimestamp,
          Optional<String> serviceCodeOpt,
          Optional<String> eventContext) throws SQLException {

    return CompletableFuture.runAsync(() -> {
      synchronized (this) {
        try {
          conn.createStatement().executeUpdate(createEventQuery(eventId, eventTimestamp, serviceCodeOpt, eventContext)); //db에 어떤 작업할 때, blocking 필요. race condition 방
        } catch (SQLException e) {
          logger.error(e.getMessage());
        }
      }
    });
  }

}
