package asynchomework.eventapi.task;

import asynchomework.eventapi.EventData;

public record TaskCreatedEvent(
    String taskPublicId,
    long assigneeId
) implements EventData {
}
