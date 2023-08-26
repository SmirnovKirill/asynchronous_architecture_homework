package asynchomework.tracker.service.domain;

import asynchomework.tracker.service.db.TaskDb;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record Task(
    long id,
    String publicId,
    String title,
    String description,
    TaskStatus status,
    TrackerUser assignee,
    BigDecimal assignFee,
    BigDecimal resolvePrice,
    OffsetDateTime creationTime
) {
  public static Task from(TaskDb taskDb) {
    return new Task(
        taskDb.getTaskId(),
        taskDb.getTaskPublicId(),
        taskDb.getTitle(),
        taskDb.getDescription(),
        taskDb.getStatus(),
        TrackerUser.from(taskDb.getAssignee()),
        taskDb.getAssignFee(),
        taskDb.getResolvePrice(),
        taskDb.getCreationTime()
    );
  }
}
