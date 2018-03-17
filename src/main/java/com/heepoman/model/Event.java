package com.heepoman.model;

import java.util.Optional;

public class Event {

  public Long eventId;
  public String eventTimestamp;
  public Optional<String> serviceCodeOpt;
  public Optional<String> eventContextOpt;

  public Event(Long eventId, String eventTimestamp, Optional<String> serviceCodeOpt, Optional<String> eventContextOpt) {
    this.eventId = eventId;
    this.eventTimestamp = eventTimestamp;
    this.serviceCodeOpt = serviceCodeOpt;
    this.eventContextOpt = eventContextOpt;
  }
}
