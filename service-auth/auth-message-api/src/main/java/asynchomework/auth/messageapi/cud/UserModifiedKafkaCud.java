package asynchomework.auth.messageapi.cud;

import asynchomework.auth.messageapi.UserRoleKafka;
import java.time.OffsetDateTime;

public record UserModifiedKafkaCud(
    ModifyTypeKafka modifyType,
    long id,
    String name,
    int beakSize,
    UserRoleKafka role,
    OffsetDateTime creationTime
) {
}
