package asynchomework.tracker.service.domain;

import asynchomework.tracker.service.db.TaskDb;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record Task(
    long id,
    String publicId,
    String title,
    String jiraId,
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
        extractTitle(taskDb.getTitle()),
        extractJiraId(taskDb.getTitle()),
        taskDb.getDescription(),
        taskDb.getStatus(),
        TrackerUser.from(taskDb.getAssignee()),
        taskDb.getAssignFee(),
        taskDb.getResolvePrice(),
        taskDb.getCreationTime()
    );
  }

  private static String extractTitle(String titleFull) {
    if (!titleFull.contains("[") || !titleFull.contains("]") || !titleFull.contains(" - ")) {
      return titleFull;
    }

    return titleFull.split(" - ")[1].trim();
  }

  private static String extractJiraId(String titleFull) {
    if (!titleFull.contains("[") || !titleFull.contains("]") || !titleFull.contains(" - ")) {
      return titleFull;
    }

    return titleFull.split(" - ")[0].replace("[", "").replace("]", "").trim();
  }
}
