package com.heepoman.repo.table;

public class EventTable {

  private final String TABLE_NAME = "event";
  private final String EVENT_ID_COLUMN = "event_id";
  private final String TIMESTAMP_COLUMN = "event_timestamp";
  private final String SERVICE_CODE_COLUMN = "service_code";
  private final String EVENT_CONTEXT_COLUMN = "event_context";

  private static class SingletonHelper {
    static final EventTable INSTANCE = new EventTable();
  }

  public static EventTable getInstance() {
    return SingletonHelper.INSTANCE;
  }

  public String getTableName() {
    return TABLE_NAME;
  }

  public String getEventIdColumn() {
    return EVENT_ID_COLUMN;
  }

  public String getTimestampColumn() {
    return TIMESTAMP_COLUMN;
  }

  public String getServiceCodeColumn() {
    return SERVICE_CODE_COLUMN;
  }

  public String getEventContextColumn() {
    return EVENT_CONTEXT_COLUMN;
  }

}
