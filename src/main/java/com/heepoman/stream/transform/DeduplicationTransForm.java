package com.heepoman.stream.transform;

import com.heepoman.model.Event;
import com.heepoman.repo.Repository;
import com.heepoman.window.EventSlidingWindow;
import com.heepoman.window.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DeduplicationTransForm implements TransForm<String> {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final Set<String> filterKeys;
  private final String targetWindow;

  public DeduplicationTransForm(Set<String> filterKeys, String targetWindow) {
    this.filterKeys = filterKeys;
    this.targetWindow = targetWindow;
  }

  private Set<Boolean> checkDuplicateByFilterKeys(Event existingEvent, Event newEvent, Set<String> filterKeys) {
    Set<Boolean> isDuplicatedByFilters = new HashSet<Boolean>();
    filterKeys.stream().forEach(filterKey -> {
        try {
          String methodPrefix = "get";
          String methodName = methodPrefix + filterKey;
          Object newEventFieldValue = newEvent.getClass().getDeclaredMethod(methodName).invoke(newEvent);
          isDuplicatedByFilters.add(existingEvent.getClass().getDeclaredMethod(methodName).invoke(existingEvent).equals(newEventFieldValue));
        } catch (NoSuchMethodException ex) {
          logger.error(ex.getMessage());
        } catch (InvocationTargetException ex) {
          logger.error(ex.getMessage());
        } catch (IllegalAccessException ex) {
          logger.error(ex.getMessage());
        }
      });
    return isDuplicatedByFilters;
  }

  @Override
  public void process(String data, Window window, Repository repo) {
    if (targetWindow.equals("event-window")) {
      EventSlidingWindow esw = (EventSlidingWindow) window;
      Event newEvent = new StringToEventMapperImpl().map(data);

      boolean isExistData = esw.getDataForDuration(newEvent).stream().anyMatch(existingEvent -> {
        Set<Boolean> isDupByFilterKeys = checkDuplicateByFilterKeys(existingEvent, newEvent, filterKeys);
        boolean isContainTrue = isDupByFilterKeys.contains(true);
        boolean isSizeOne = isDupByFilterKeys.size() == 1;
        return isContainTrue && isSizeOne;
      });
      if(!isExistData) {
        repo.add(Optional.of(newEvent)).thenRun(() -> {
          esw.setData(newEvent);
          logger.info("this event data successfully loaded to target database : " + newEvent);
          });
        } else {
          logger.info(String.format("this event data already exist on window size. %s", newEvent));
        }
    } else {
      throw new InvalidParameterException("your target window is invalid. make sure the target window is `event-window`");
      //TODO: can be implemented other event window type & refactoring for flexible.
    }
  }

}
