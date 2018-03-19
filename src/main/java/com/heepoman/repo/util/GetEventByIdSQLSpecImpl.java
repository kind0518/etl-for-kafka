package com.heepoman.repo.util;

import com.heepoman.repo.table.EventTable;

public class GetEventByIdSQLSpecImpl implements SQLSpec {

  private final Long eventId;
  private final EventTable eventTable = EventTable.getInstance();

  public GetEventByIdSQLSpecImpl(final Long id) {
    this.eventId = id;
  }

  @Override
  public String toQuery() {
    return String.format(
            "SELECT * FROM %s WHERE %s = %d;",
            eventTable.getTableName(),
            eventTable.getEventIdColumn(),
            eventId
    );
  }

}
