package com.heepoman.stream.transform;

import com.heepoman.model.Event;
import com.heepoman.repo.EventMySqlRepository;
import com.heepoman.window.EventSlidingWindow;
import com.heepoman.window.Window;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.*;

public class DeduplicationTransFormTest extends TransFormTestKit {

  TransForm deduplicationTransform = new DeduplicationTransForm("eventId", "event-window");

  Window slidingWindow = mock(EventSlidingWindow.class);
  EventMySqlRepository eventRepo = mock(EventMySqlRepository.class);

  @Test
  public void processWhenNotDuplicated() throws NoSuchFieldException, IllegalAccessException {
    long notDuplicatedEventId = 12345678911L;
    String payload = String.format(
            "event_id: %d, event_timestamp: 2018-03-18T07:10:29+0000, service_code: SERVICE_CODE, event_context: EVENT_CONTEXT", notDuplicatedEventId);
    when(slidingWindow.getWindowTable()).thenReturn(eventTable);
    when(eventRepo.add(any())).thenReturn(CompletableFuture.completedFuture(null));

    deduplicationTransform.process(payload, slidingWindow, eventRepo);
  }

  @Test
  public void processWhenDuplicated() {
    String payloadForDuplicated = String.format(
            "event_id: %d, event_timestamp: 2018-03-18T07:10:29+0000, service_code: SERVICE_CODE, event_context: EVENT_CONTEXT", eventId);
    when(slidingWindow.getWindowTable()).thenReturn(eventTable);

    deduplicationTransform.process(payloadForDuplicated, slidingWindow, eventRepo);
  }
}
