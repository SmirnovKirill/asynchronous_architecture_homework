package asynchomework.tracker.service.domain;

import asynchomework.tracker.service.db.TrackerUserDb;
import java.time.OffsetDateTime;

public record TrackerUser(
    long id,
    String publicId,
    String name,
    TrackerUserRole role,
    OffsetDateTime creationTime
) {
  public static TrackerUser from(TrackerUserDb userDb) {
    return new TrackerUser(userDb.getUserId(), userDb.getUserPublicId(), userDb.getName(), userDb.getRole(), userDb.getCreationTime());
  }
}
