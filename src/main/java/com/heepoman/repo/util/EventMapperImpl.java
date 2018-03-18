package com.heepoman.repo.util;

import com.heepoman.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class EventMapperImpl implements Mapper<ResultSet, Optional<Event>>{

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private static final String EVENT_ID = "event_id";
  private static final String EVENT_TIMESTAMP = "event_timestamp";
  private static final String SERVICE_CODE = "service_code";
  private static final String EVENT_CONTEXT = "event_context";

  @Override
  public Optional<Event> map(ResultSet rs) {
    try {
      if (rs.first()) {
        Long eventId = rs.getLong(EVENT_ID);
        String eventTimestamp = rs.getString(EVENT_TIMESTAMP);
        Optional<String> serviceCodeOpt = Optional.ofNullable(rs.getString(SERVICE_CODE));
        Optional<String> eventContextOpt = Optional.ofNullable(rs.getString(EVENT_CONTEXT));
        return Optional.of(new Event(eventId, eventTimestamp, serviceCodeOpt, eventContextOpt));
      } else {
        return Optional.empty();
      }
    } catch (SQLException ex) {
      logger.error(ex.getMessage());
      return Optional.empty();
    }
  }
}
