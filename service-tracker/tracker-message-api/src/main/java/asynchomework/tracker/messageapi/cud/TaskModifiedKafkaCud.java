package asynchomework.tracker.messageapi.cud;

import asynchomework.tracker.messageapi.TaskStatusKafka;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TaskModifiedKafkaCud(
    ModifyTypeKafka modifyType,
    long id,
    String title,
    String description,
    TaskStatusKafka status,
    long assigneeId,
    BigDecimal assignFee,
    BigDecimal resolvePrice,
    OffsetDateTime creationTime
) {
}
