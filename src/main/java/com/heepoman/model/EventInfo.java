package com.heepoman.model;

import com.heepoman.repo.table.EventTable;

public class EventInfo {

  private final String EVENT_ID_FIELD = "eventId";
  private final String TIMESTAMP_FIELD = "eventTimestamp";
  private final String SERVICE_CODE_FIELD = "serviceCodeOpt";
  private final String EVENT_CONTEXT_FIELD = "eventContextOpt";

  private static class SingletonHelper {
    static final EventInfo INSTANCE = new EventInfo();
  }

  public static EventInfo getInstance() {
    return EventInfo.SingletonHelper.INSTANCE;
  }

  public String getEventIdField() {
    return EVENT_ID_FIELD;
  }

  public String getTimestampField() {
    return TIMESTAMP_FIELD;
  }

  public String getServiceCodeField() {
    return SERVICE_CODE_FIELD;
  }

  public String getEventContextField() {
    return EVENT_CONTEXT_FIELD;
  }
}
