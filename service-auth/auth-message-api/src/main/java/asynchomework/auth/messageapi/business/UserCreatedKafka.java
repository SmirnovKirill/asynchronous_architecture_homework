package asynchomework.auth.messageapi.business;

import asynchomework.auth.messageapi.UserRoleKafka;
import java.time.OffsetDateTime;

public record UserCreatedKafka(
    long id,
    String name,
    int beakSize,
    UserRoleKafka role,
    OffsetDateTime creationTime
) {
}
