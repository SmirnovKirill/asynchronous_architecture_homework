package asynchomework.tracker.messageapi.business;

public record TaskResolvedKafka(
    long taskId,
    long assigneeId
) {
}
