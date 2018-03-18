package com.heepoman.window;

import com.heepoman.model.Event;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class EventSlidingWindowTest {

  private static final long TEN_MINUTES = 10 * 60 * 1000;

  Window sWindow = new EventSlidingWindow(TEN_MINUTES);

  @Test
  public void setData() {
    Long eventId = 12345678910L;
    String newDate = "2018-03-18T07:22:29+0000";
    String serviceCode = "SERVICE_CODE";
    String eventContext = "EVENT_CONTEXT";

    Event newEvent = new Event(eventId, newDate, Optional.of(serviceCode), Optional.of(eventContext));

    assertTrue(sWindow.setData(newEvent));
  }
}
