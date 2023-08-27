package asynchomework.eventapi.user;

import asynchomework.eventapi.EventData;
import asynchomework.eventapi.EventName;
import asynchomework.eventapi.StreamEventType;
import java.time.OffsetDateTime;

public record UserStreamEvent(
  StreamEventType eventType,
  String userPublicId,
  String name,
  int beakSize,
  UserRole role,
  OffsetDateTime creationTime
) implements EventData {
  @Override
  public EventName getEventName() {
    return EventName.USER_STREAM;
  }
}
