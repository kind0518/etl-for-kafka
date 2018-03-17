package com.heepoman.repo.util;

public class GetEventByIdSQLSpecImpl implements SQLSpec {
  private Long eventId;
  private static final String TABLE_NAME = "event";
  private static final String COLUMN_NAME = "event_id";

  public GetEventByIdSQLSpecImpl(final Long id) {
    this.eventId = id;
  }

  @Override
  public String toQuery() {
    return String.format(
            "SELECT * FROM %s WHERE %s = %d;",
            TABLE_NAME,
            COLUMN_NAME,
            eventId
    );
  }
}
