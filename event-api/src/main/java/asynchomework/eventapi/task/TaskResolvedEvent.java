package asynchomework.eventapi.task;

import asynchomework.eventapi.EventData;

public record TaskResolvedEvent(
    String taskPublicId,
    long assigneeId
) implements EventData {
}
