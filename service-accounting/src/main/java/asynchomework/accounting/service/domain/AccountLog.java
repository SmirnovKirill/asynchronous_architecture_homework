package asynchomework.accounting.service.domain;

import asynchomework.accounting.service.db.AccountLogDb;
import asynchomework.accounting.service.db.AccountingTaskDb;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

public record AccountLog(
    long logId,
    Account account,
    OperationType operationType,
    BigDecimal amount,
    OffsetDateTime operationTime,
    AccountingTask task
) {
  public static AccountLog from(AccountLogDb accountLogDb, AccountingTaskDb taskDb) {
    return new AccountLog(
        accountLogDb.getAccountLogId(),
        Account.from(accountLogDb.getAccount()),
        accountLogDb.getOperationType(),
        accountLogDb.getAmount(),
        accountLogDb.getOperationTime(),
        Optional.ofNullable(taskDb).map(task -> AccountingTask.from(taskDb)).orElse(null)
    );
  }
}
