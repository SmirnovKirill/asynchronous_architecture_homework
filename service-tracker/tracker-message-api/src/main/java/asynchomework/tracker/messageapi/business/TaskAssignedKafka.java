package asynchomework.tracker.messageapi.business;

public record TaskAssignedKafka(
    long taskId,
    long assigneeId
) {
}
