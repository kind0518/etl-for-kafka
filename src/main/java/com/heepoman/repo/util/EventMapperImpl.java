package com.heepoman.repo.util;

import com.heepoman.model.Event;
import com.heepoman.repo.table.EventTable;
import com.heepoman.util.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class EventMapperImpl implements Mapper<ResultSet, Optional<Event>> {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private final EventTable eventTable = EventTable.getInstance();

  @Override
  public Optional<Event> map(ResultSet rs) {
    try {
      if (rs.first()) {
        Long eventId = rs.getLong(eventTable.getEventIdColumn());
        String eventTimestamp = rs.getString(eventTable.getTimestampColumn());
        Optional<String> serviceCodeOpt = Optional.ofNullable(rs.getString(eventTable.getServiceCodeColumn()));
        Optional<String> eventContextOpt = Optional.ofNullable(rs.getString(eventTable.getEventContextColumn()));
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
