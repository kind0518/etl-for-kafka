package com.heepoman.stream.transform;

import com.heepoman.model.Event;
import com.heepoman.util.Mapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class StringToEventMapperImpl implements Mapper<String, Event> {

  @Override
  public Event map(String payload) {
    final HashMap<String, String> holder = new HashMap();

    Arrays.stream(payload.replaceAll(" ", "").trim().split(","))
            .map(s -> s.split(":", 2))
            .forEach(s -> holder.put(s[0], s[1]));

    return new Event(
            Long.valueOf(holder.get("event_id")),
            holder.get("event_timestamp"),
            Optional.ofNullable(holder.get("service_code")),
            Optional.ofNullable(holder.get("event_context")));
  }

}
