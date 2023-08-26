package asynchomework.accounting.service.domain;

import asynchomework.accounting.service.db.AccountingTaskDb;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AccountingTask(
    long taskId,
    String taskPublicId,

    String title,
    
    AccountingUser assignee,
    
    BigDecimal assignFee,
    
    BigDecimal resolvePrice,
    OffsetDateTime creationTime
) {
  public static AccountingTask from(AccountingTaskDb taskDb) {
    return new AccountingTask(
        taskDb.getTaskId(),
        taskDb.getTaskPublicId(),
        taskDb.getTitle(),
        AccountingUser.from(taskDb.getAssignee()),
        taskDb.getAssignFee(),
        taskDb.getResolvePrice(),
        taskDb.getCreationTime()
    );
  }
}
