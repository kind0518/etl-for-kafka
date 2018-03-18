package com.heepoman.repo.util;

import com.heepoman.repo.table.EventTable;

public class GetEventByIdSQLSpecImpl implements SQLSpec {
  private Long eventId;
  static final EventTable eventTable = EventTable.getInstance();

  public GetEventByIdSQLSpecImpl(final Long id) {
    this.eventId = id;

  }

  @Override
  public String toQuery() {
    return String.format(
            "SELECT * FROM %s WHERE %s = %d;",
            eventTable.TABLE_NAME,
            eventTable.EVENT_ID_COLUMN,
            eventId
    );
  }
}
