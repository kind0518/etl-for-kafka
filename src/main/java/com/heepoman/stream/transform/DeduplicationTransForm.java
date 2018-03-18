package com.heepoman.stream.transform;

import com.heepoman.model.Event;
import com.heepoman.repo.Repository;
import com.heepoman.window.EventSlidingWindow;
import com.heepoman.window.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.Optional;

public class DeduplicationTransForm implements TransForm<String> {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private String filterKey;
  private String targetWindow;

  public DeduplicationTransForm(String filterKey, String targetWindow) {
    this.filterKey = filterKey;
    this.targetWindow = targetWindow;
  }

  @Override
  public void process(String data, Window window, Repository repo) {
    if (this.targetWindow.equals("event-window")) {
      synchronized (this) {
        EventSlidingWindow esw = (EventSlidingWindow) window;
        Event eventData = new StringToEventMapperImpl().map(data);
        esw.keepWindowSizeForDuration(eventData);

        boolean isExistData = esw.getWindowTable().stream().anyMatch(e -> {
          try {
            return e.getClass().getDeclaredField(this.filterKey).get(e).equals(eventData.eventId);
          } catch (NoSuchFieldException ex) {
            logger.error(ex.getMessage());
            return false;
          } catch (IllegalAccessException ex) {
            logger.error(ex.getMessage());
            return false;
          }
        });
        if(!isExistData) {
          repo.add(Optional.of(eventData)).thenRun(() -> {
            esw.setData(eventData);
          });
        } else {
          logger.info(String.format("this event data already exist on window size. %s", eventData));
        }
      }
    } else {
      throw new InvalidParameterException("your target window is invalid. make sure the target window is `event-window`");
      //TODO: can be implemented other event window type & refactoring for flexible.
    }

  }
}
