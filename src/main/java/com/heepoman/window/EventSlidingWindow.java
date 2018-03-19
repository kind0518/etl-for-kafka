package com.heepoman.window;

import com.heepoman.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;

public class EventSlidingWindow extends Window<Event> {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private long duration;

  public EventSlidingWindow(long duration) {
    this.duration = duration;
  }

  private Date convertToDate(String timestamp) throws ParseException {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    TimeZone tz = TimeZone.getTimeZone("UTC");
    format.setTimeZone(tz);
    return format.parse(timestamp);
  }

  private boolean isDataTimeBeforeDuration(String targetTimestamp, String currentTimestamp, long duration) {
    try {
      Date targetDate = convertToDate(targetTimestamp);
      Date currentDate = convertToDate(currentTimestamp);
      Date beforeDurationDate = new Date(currentDate.getTime() - duration);
      return targetDate.before(beforeDurationDate);
    } catch (ParseException ex) {
      logger.error(ex.getMessage());
      return false;
    }
  }

  @Override
  public void keepWindowSizeForDuration(Event data) {
    synchronized(this){
      eventWindowTable.removeIf((Event event) ->
              isDataTimeBeforeDuration(
                      event.getEventTimestamp(),
                      data.getEventTimestamp(),
                      this.duration));
    }
  }

  @Override
  public boolean setData(Event data) {
    return eventWindowTable.add(data);
  }

  @Override
  public BlockingQueue<Event> getWindowTable() {
    return this.eventWindowTable;
  }
}