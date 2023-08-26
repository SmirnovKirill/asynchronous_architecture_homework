package asynchomework.eventapi.task;

import asynchomework.eventapi.EventData;
import asynchomework.eventapi.EventName;
import java.time.OffsetDateTime;

public record TaskResolvedEvent(
    String taskPublicId,
    long assigneeId,
    OffsetDateTime resolveTime
) implements EventData {
  @Override
  public EventName getEventName() {
    return EventName.TASK_RESOLVED;
  }
}
