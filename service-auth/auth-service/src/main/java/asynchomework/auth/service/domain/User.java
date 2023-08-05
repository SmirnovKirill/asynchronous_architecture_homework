package asynchomework.auth.service.domain;

import java.time.OffsetDateTime;

public record User(
    long id,
    String name,
    int beakSize,
    UserRole role,
    OffsetDateTime creationTime
) {
}
