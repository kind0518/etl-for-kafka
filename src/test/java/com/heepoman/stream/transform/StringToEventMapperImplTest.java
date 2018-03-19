package com.heepoman.stream.transform;

import com.heepoman.model.Event;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class StringToEventMapperImplTest {

  StringToEventMapperImpl mapper = new StringToEventMapperImpl();

  @Test
  public void stringToEventMapperTest() {
    String payload = "event_id: 1, event_timestamp: 2018-03-18T07:10:29+0000, service_code: SERVICE_CODE, event_context: EVENT_CONTEXT";
    Event expectedEvent = new Event(1L, "2018-03-18T07:10:29+0000", Optional.of("SERVICE_CODE"), Optional.of("EVENT_CONTEXT"));

    assertEquals(expectedEvent.getEventId(), mapper.map(payload).getEventId());
    assertEquals(expectedEvent.getEventTimestamp(), mapper.map(payload).getEventTimestamp());
    assertEquals(expectedEvent.getServiceCodeOpt(), mapper.map(payload).getServiceCodeOpt());
    assertEquals(expectedEvent.getEventContextOpt(), mapper.map(payload).getEventContextOpt());
  }

}
