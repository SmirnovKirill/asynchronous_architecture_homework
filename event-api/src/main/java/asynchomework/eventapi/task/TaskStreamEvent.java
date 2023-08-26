package asynchomework.eventapi.task;

import asynchomework.eventapi.EventData;
import asynchomework.eventapi.EventName;
import asynchomework.eventapi.StreamEventType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TaskStreamEvent(
    StreamEventType eventType,
    String taskPublicId,
    String title,
    String jiraId,
    String description,
    TaskStatus status,
    long assigneeId,
    BigDecimal assignFee,
    BigDecimal resolvePrice,
    OffsetDateTime creationTime
) implements EventData {
  @Override
  public EventName getEventName() {
    return EventName.TASK_STREAM;
  }
}
