package com.heepoman.repo.pool;

import com.heepoman.repo.exception.ConnectionPoolException;
import com.heepoman.repo.driver.RepositoryDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.*;

public class ConnectionPoolImpl implements ConnectionPool {

  private static final int DEFAULT_TIMEOUT = 5;
  private static final int DEFAULT_POOL_SIZE = 5;
  private static final int HEALTH_CHECK_INTERVAL = 5;

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private int poolSize;
  private BlockingQueue<Connection> connectionPool;
  private RepositoryDriver driver;
  private ScheduledExecutorService checkHealthPool = Executors.newSingleThreadScheduledExecutor();

  private Boolean isClosedForConnection(Connection c) {
    try {
      return c.isClosed();
    } catch (SQLException ex) {
      logger.error(ex.getMessage());
      return true;
    }
  }

  public ConnectionPoolImpl(ConnectionPoolBuilder builder) {
    this.driver = builder.driver;
    this.poolSize = builder.poolSize;
    this.connectionPool = builder.connectionPool;
    this.checkHealthPool.scheduleAtFixedRate(() -> {
      connectionPool
        .stream()
        .filter(c -> isClosedForConnection(c))
        .forEach(c -> connectionPool.remove(c));
    }, HEALTH_CHECK_INTERVAL, HEALTH_CHECK_INTERVAL, TimeUnit.SECONDS);
  }

  private void createConnection() {
    while(connectionPool.size() < poolSize) {
      try {
        connectionPool.put(driver.getConnection());
      } catch (SQLException ex) {
        logger.error(ex.getMessage());
      } catch (InterruptedException ex) {
        logger.error(ex.getMessage());
      }
    }
  }

  @Override
  public synchronized Connection getConnectionFromPool() throws ConnectionPoolException {
    try {
      if (connectionPool.isEmpty()) this.createConnection();
      return connectionPool.take();
    } catch (InterruptedException ex) {
      logger.error(ex.getMessage());
      throw new ConnectionPoolException(ex.getMessage());
    }
  }

  public static class ConnectionPoolBuilder {
    private int poolSize;
    private BlockingQueue<Connection> connectionPool;
    private RepositoryDriver driver;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ConnectionPoolBuilder(RepositoryDriver driver) {
      this.driver = driver;
      this.poolSize = DEFAULT_POOL_SIZE;
      this.connectionPool = init(this.poolSize);
    }

    private BlockingQueue<Connection> init(int poolSize) {
      BlockingQueue<Connection> pool = new LinkedBlockingQueue<Connection>(poolSize);
      for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
        try {
          pool.put(this.driver.getConnection());
        } catch (SQLException ex) {
          logger.error(ex.getMessage());
        } catch (InterruptedException ex) {
          logger.error(ex.getMessage());
        }
      }
      return pool;
    }

    public ConnectionPoolBuilder setPoolSize(int poolSize) {
      this.poolSize = poolSize;
      this.connectionPool = init(poolSize);
      return this;
    }

    public ConnectionPoolImpl build() {
      return new ConnectionPoolImpl(this);
    }
  }

}
