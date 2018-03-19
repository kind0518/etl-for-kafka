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

  private final EventMySqlRepository repo = new EventMySqlRepository();
  private final Mapper<ResultSet, Optional<Event>> toEventOpt = new EventMapperImpl();

  @Test
  public void add() throws SQLException, ExecutionException, InterruptedException{
    final Long eventId = 12345678910L;
    final Event event = new Event(eventId, eventTimestamp, Optional.of(serviceCode), Optional.of(eventContext));
    final CompletableFuture<Void> resultF = repo.add(Optional.of(event));

    assertEquals(CompletableFuture.completedFuture(null).get(), resultF.get());
  }

  @Test
  public void getEventData() throws SQLException, ConnectionPoolException, InterruptedException, ExecutionException {
    final Long eventId = 12345678911L;
    final Event result = repo.query(new GetEventByIdSQLSpecImpl(eventId)).get().get();

    assertTrue(result.getEventId().equals(eventId));
    assertTrue(result.getEventContextOpt().get().equals(eventContext));
    assertTrue(result.getServiceCodeOpt().get().equals(serviceCode));
    assertNotNull(result.getEventTimestamp());
  }

}
