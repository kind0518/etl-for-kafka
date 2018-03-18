package com.heepoman.repo;

import com.heepoman.model.Event;
import com.heepoman.repo.exception.ConnectionPoolException;
import com.heepoman.repo.util.EventMapperImpl;
import com.heepoman.repo.util.GetEventByIdSQLSpecImpl;
import com.heepoman.util.Mapper;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class EventMySqlRepositoryTest extends MySqlRepositoryTestKit {
  EventMySqlRepository repo = new EventMySqlRepository();
  private final Mapper<ResultSet, Optional<Event>> toEventOpt = new EventMapperImpl();

  @Test
  public void add() throws SQLException, ExecutionException, InterruptedException{
    Long eventId = 12345678910L;
    Event event = new Event(eventId, eventTimestamp, Optional.of(serviceCode), Optional.of(eventContext));

    CompletableFuture<Void> resultF = repo.add(Optional.of(event));
    assertEquals(CompletableFuture.completedFuture(null).get(), resultF.get());
  }

  @Test
  public void getEventData() throws SQLException, ConnectionPoolException, InterruptedException, ExecutionException {
    Long eventId = 12345678911L;

    Event result = repo.query(new GetEventByIdSQLSpecImpl(eventId)).get().get();
    assertTrue(result.eventId.equals(eventId));
    assertTrue(result.eventContextOpt.get().equals(eventContext));
    assertTrue(result.serviceCodeOpt.get().equals(serviceCode));
    assertNotNull(result.eventTimestamp);
  }
}
