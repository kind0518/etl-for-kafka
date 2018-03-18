package com.heepoman.stream.transform;

import com.heepoman.model.Event;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class StringToEventMapperImplTest {

  StringToEventMapperImpl mapper = new StringToEventMapperImpl();

  @Test
  public void stringToEventMapperTest() {
    String payload = "event_id: 1, event_timestamp: 2018-03-18T07:10:29+0000, service_code: SERVICE_CODE, event_context: EVENT_CONTEXT";
    Event expectedEvent = new Event(1L, "2018-03-18T07:10:29+0000", Optional.of("SERVICE_CODE"), Optional.of("EVENT_CONTEXT"));

    assertEquals(expectedEvent.eventId, mapper.map(payload).eventId);
    assertEquals(expectedEvent.eventTimestamp, mapper.map(payload).eventTimestamp);
    assertEquals(expectedEvent.serviceCodeOpt, mapper.map(payload).serviceCodeOpt);
    assertEquals(expectedEvent.eventContextOpt, mapper.map(payload).eventContextOpt);
  }
}
