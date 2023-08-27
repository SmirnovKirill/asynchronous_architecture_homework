package asynchomework.eventapi.user;

import asynchomework.eventapi.EventData;
import asynchomework.eventapi.EventName;
import java.time.OffsetDateTime;

public record UserCreatedEvent(
    String userPublicId,
    String name,
    int beakSize,
    UserRole role,
    OffsetDateTime creationTime
) implements EventData {
  @Override
  public EventName getEventName() {
    return EventName.USER_CREATED;
  }
}
