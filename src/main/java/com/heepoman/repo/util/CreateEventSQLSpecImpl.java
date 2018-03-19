package com.heepoman.repo.util;

import com.heepoman.repo.table.EventTable;

import java.util.Optional;

public class CreateEventSQLSpecImpl implements SQLSpec {

  private static final EventTable eventTable = EventTable.getInstance();
  private Long eventId;
  private String eventTimestamp;
  private Optional<String> serviceCodeOpt;
  private Optional<String> eventCodeOpt;

  public CreateEventSQLSpecImpl(Long eventId, String eventTimestamp, Optional<String> serviceCodeOpt, Optional<String> eventCodeOpt) {
    this.eventId = eventId;
    this.eventTimestamp = eventTimestamp;
    this.serviceCodeOpt = serviceCodeOpt;
    this.eventCodeOpt = eventCodeOpt;
  }

  @Override
  public String toQuery() {
    return String.format(
            "INSERT INTO %s VALUES (%d, '%s', '%s', '%s')",
            eventTable.getTableName(),
            eventId,
            eventTimestamp,
            serviceCodeOpt.orElse(null),
            eventCodeOpt.orElse(null)
    );
  }
}

