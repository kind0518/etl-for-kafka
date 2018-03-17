package com.heepoman.repo;

import com.heepoman.repo.exception.ConnectionPoolException;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MySqlRepositoryTest extends MySqlRepositoryTestKit {
  MySqlRepository repo = new MySqlRepository();

  @Test
  public void createEventData() throws SQLException, InterruptedException, ExecutionException {
    Long eventId = 12345678910L;
    String timestamp = new Date().toString();
    Optional<String> serviceCodeOpt = Optional.of("SERVICE_CODE");
    Optional<String> eventContextOpt = Optional.of("EVENT_CONTEXT");

    CompletableFuture<Void> resultF = repo.createEventData(eventId, timestamp, serviceCodeOpt, eventContextOpt);
    Assert.assertEquals(CompletableFuture.completedFuture(null).get(), resultF.get());
  }

  @Test
  public void getEventData() throws SQLException, ConnectionPoolException, InterruptedException, ExecutionException{
    Long eventId = 12345678911L;
    CompletableFuture<ResultSet> resultF = repo.getEventData(eventId);
    Assert.assertTrue(resultF.get().next());
  }
}
