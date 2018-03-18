package com.heepoman.stream.transform;

import com.heepoman.model.Event;
import org.junit.Before;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TransFormTestKit {

  protected final Long eventId = 12345678910L;
  protected final String datetime  = "2018-03-18T07:10:29+0000";
  protected final String serviceCode = "SERVICE_CODE";
  protected final String eventContext = "EVENT_CONTEXT";
  BlockingQueue<Event> eventTable;

  @Before
  public void init() {
    this.eventTable = new LinkedBlockingQueue<Event>();
    this.eventTable.add(new Event(eventId, datetime, Optional.of(serviceCode), Optional.of(eventContext)));
  }
}
