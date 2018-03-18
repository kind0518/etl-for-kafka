package com.heepoman.repo.table;

public class EventTable {

  public final String TABLE_NAME = "event";
  public final String EVENT_ID_COLUMN = "event_id";
  public final String TIMESTAMP_COLUMN = "event_timestamp";
  public final String SERVICE_CODE_COLUMN = "service_code";
  public final String EVENT_CONTEXT_COLUMN = "event_context";

  private static class SingletonHelper {
    static final EventTable INSTANCE = new EventTable();
  }

  public static EventTable getInstance() {
    return SingletonHelper.INSTANCE;
  }

}
