package asynchomework.eventapi;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property="@eventDataClass")
public interface EventData {
  EventName getEventName();
}
