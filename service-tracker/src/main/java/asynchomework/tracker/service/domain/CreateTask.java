package asynchomework.tracker.service.domain;

import java.time.OffsetDateTime;

public record CreateTask(
    String title,
    String description,
    OffsetDateTime creationTime
) {
}
