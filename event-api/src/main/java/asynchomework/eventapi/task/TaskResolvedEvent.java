package asynchomework.eventapi.task;

import asynchomework.eventapi.EventData;
import asynchomework.eventapi.EventName;

public record TaskResolvedEvent(
    String taskPublicId,
    long assigneeId
) implements EventData {
  @Override
  public EventName getEventName() {
    return EventName.TASK_RESOLVED;
  }
}
