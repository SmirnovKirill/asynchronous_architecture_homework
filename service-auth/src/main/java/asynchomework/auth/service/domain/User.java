package asynchomework.auth.service.domain;

import asynchomework.auth.service.db.PopugUserDb;
import java.time.OffsetDateTime;

public record User(
    long id,
    String publicId,
    String name,
    int beakSize,
    UserRole role,
    OffsetDateTime creationTime
) {
  public static User from(PopugUserDb popugUserDb) {
    return new User(
        popugUserDb.getUserId(),
        popugUserDb.getUserPublicId(),
        popugUserDb.getName(),
        popugUserDb.getBeakSize(),
        popugUserDb.getRole(),
        popugUserDb.getCreationTime()
    );
  }
}
