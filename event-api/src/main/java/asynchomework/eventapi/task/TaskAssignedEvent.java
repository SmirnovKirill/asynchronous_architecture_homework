package asynchomework.eventapi.task;

import asynchomework.eventapi.EventData;

public record TaskAssignedEvent(
    String taskPublicId,
    long assigneeId
) implements EventData {
}
