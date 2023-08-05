package asynchomework.auth.api;

import java.time.OffsetDateTime;

public record UserDto(
    long id,
    String name,
    int beakSize,
    UserRoleDto role,
    OffsetDateTime creationTime
) {
}

