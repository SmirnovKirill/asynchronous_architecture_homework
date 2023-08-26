package asynchomework.eventapi.task;

import asynchomework.eventapi.EventData;
import asynchomework.eventapi.EventName;
import java.time.OffsetDateTime;

public record TaskCreatedEvent(
    String taskPublicId,
    long assigneeId,
    OffsetDateTime creationTime
) implements EventData {
  @Override
  public EventName getEventName() {
    return EventName.TASK_CREATED;
  }
}
