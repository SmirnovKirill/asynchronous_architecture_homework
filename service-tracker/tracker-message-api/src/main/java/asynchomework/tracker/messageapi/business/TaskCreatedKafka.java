package asynchomework.tracker.messageapi.business;

public record TaskCreatedKafka(
    long taskId,
    long assigneeId
) {
}
