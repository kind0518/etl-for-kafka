package com.heepoman.model;

import java.util.Optional;

public class Event {

  private Long eventId; //required
  private String eventTimestamp; //required
  private Optional<String> serviceCodeOpt; //option value
  private Optional<String> eventContextOpt; //option value

  public Event(Long eventId, String eventTimestamp, Optional<String> serviceCodeOpt, Optional<String> eventContextOpt) {
    this.eventId = eventId;
    this.eventTimestamp = eventTimestamp;
    this.serviceCodeOpt = serviceCodeOpt;
    this.eventContextOpt = eventContextOpt;
  }
}
