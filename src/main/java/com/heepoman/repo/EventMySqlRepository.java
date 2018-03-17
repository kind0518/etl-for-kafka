package com.heepoman.repo;

import com.heepoman.model.Event;
import com.heepoman.repo.driver.MySqlDriver;
import com.heepoman.repo.exception.ConnectionPoolException;
import com.heepoman.repo.pool.ConnectionPool;
import com.heepoman.repo.pool.ConnectionPoolImpl;
import com.heepoman.repo.util.Mapper;
import com.heepoman.repo.util.EventMapperImpl;
import com.heepoman.repo.util.SQLSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class EventMySqlRepository implements Repository<Optional<Event>> {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private ConnectionPool repo;
  private final Mapper<ResultSet, Optional<Event>> toEventOpt = new EventMapperImpl();

  public EventMySqlRepository() {
    this.repo = new ConnectionPoolImpl.ConnectionPoolBuilder(new MySqlDriver()).build();
  }

  @Override
  public CompletableFuture<Void> add(Optional<Event> eventOpt) {
    return CompletableFuture.runAsync(() -> {
      synchronized (this) {
        Optional<Connection> connOpt = Optional.empty();
        Optional<PreparedStatement> stmtOpt = Optional.empty();
        try {
          connOpt = Optional.of(repo.getConnectionFromPool());
          stmtOpt = Optional.of(connOpt.get().prepareStatement("INSERT INTO event(event_id, event_timestamp, service_code, event_context) VALUES (?, ?, ?, ?)"));
          if(eventOpt.isPresent()) {
            final Event e = eventOpt.get();
            stmtOpt.get().setLong(1, e.eventId);
            stmtOpt.get().setString(2, e.eventTimestamp);
            stmtOpt.get().setString(3, e.serviceCodeOpt.orElse(null));
            stmtOpt.get().setString(4, e.eventContextOpt.orElse(null));
            stmtOpt.get().executeUpdate();
          } else {
            logger.error("parameter `event` does not exist.");
          }
        } catch (ConnectionPoolException ex) {
          logger.error(ex.getMessage());
        } catch (SQLException ex) {
          logger.error(ex.getMessage());
        } finally {
          if(connOpt.isPresent()) {
            try {
              connOpt.get().close();
            } catch (SQLException ex) {
              logger.error(ex.getMessage());
            }
          }
          if(stmtOpt.isPresent()) {
            try {
              stmtOpt.get().close();
            } catch (SQLException ex) {
              logger.error(ex.getMessage());
            }
          }
        }
      }
    });
  }

  @Override
  public CompletableFuture<Optional<Event>> query(SQLSpec spec) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        Connection conn = repo.getConnectionFromPool();
        Statement stmt = conn.createStatement();
        return toEventOpt.map(stmt.executeQuery(spec.toQuery()));
      } catch (SQLException ex) {
        logger.error(ex.getMessage());
        return Optional.empty();
      } catch (ConnectionPoolException ex) {
        logger.error(ex.getMessage());
        return Optional.empty();
      }
    });
  }

  @Override
  public CompletableFuture<Void> update(Optional<Event> event) {
    //TODO: It is just an interface sketch. It can be implemented later.
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public CompletableFuture<Void> remove(Optional<Event> event) {
    //TODO: It is just an interface sketch. It can be implemented later.
    return CompletableFuture.completedFuture(null);
  }

}