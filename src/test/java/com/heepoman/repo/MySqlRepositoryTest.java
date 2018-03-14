package com.heepoman.repo;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MySqlRepositoryTest {

  MySqlRepository repo = new MySqlRepository();

  @Test
  public void createEventData() throws SQLException, InterruptedException, ExecutionException {
    Long eventId = 12345678910L;
    String timestamp = new Date().toString();
    Optional<String> serviceCodeOpt = Optional.of("service_code");
    Optional<String> eventContextOpt = Optional.of("event_context");

    CompletableFuture<Void> resultF = repo.createEventData(eventId, timestamp, serviceCodeOpt, eventContextOpt);
    CompletableFuture<Void> resultF2 = repo.createEventData(eventId, timestamp, serviceCodeOpt, eventContextOpt);
    CompletableFuture<Void> resultF3 = repo.createEventData(eventId, timestamp, serviceCodeOpt, eventContextOpt);
    CompletableFuture<Void> resultF4 = repo.createEventData(eventId, timestamp, serviceCodeOpt, eventContextOpt);

    Assert.assertEquals(resultF.get(), CompletableFuture.runAsync(() -> {}).get());
    Assert.assertEquals(resultF2.get(), CompletableFuture.runAsync(() -> {}).get());
    Assert.assertEquals(resultF3.get(), CompletableFuture.runAsync(() -> {}).get());
    Assert.assertEquals(resultF4.get(), CompletableFuture.runAsync(() -> {}).get());

  }

  @Test
  public void getEventData() throws SQLException {
    Long eventId = 12345678910L;

//    repo.getEventData(eventId);
  }
}
