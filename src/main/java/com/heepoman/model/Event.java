package com.heepoman.model;

import java.util.Optional;

public class Event {

  private final Long eventId;
  private final String eventTimestamp;
  private final Optional<String> serviceCodeOpt;
  private final Optional<String> eventContextOpt;

  public Event(Long eventId, String eventTimestamp, Optional<String> serviceCodeOpt, Optional<String> eventContextOpt) {
    this.eventId = eventId;
    this.eventTimestamp = eventTimestamp;
    this.serviceCodeOpt = serviceCodeOpt;
    this.eventContextOpt = eventContextOpt;
  }

  public Long getEventId() {
    return eventId;
  }

  public String getEventTimestamp() {
    return eventTimestamp;
  }

  public Optional<String> getServiceCodeOpt() {
    return serviceCodeOpt;
  }

  public Optional<String> getEventContextOpt() {
    return eventContextOpt;
  }

}
