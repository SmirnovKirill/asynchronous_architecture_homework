package asynchomework.accounting.service.domain;

import asynchomework.accounting.service.db.AccountDb;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record Account(
  long accountId,
  String accountPublicId,
  AccountingUser user,
  BigDecimal balance,
  OffsetDateTime creationTime
) {
  public static Account from(AccountDb accountDb) {
    return new Account(
        accountDb.getAccountId(),
        accountDb.getAccountPublicId(),
        AccountingUser.from(accountDb.getUser()),
        accountDb.getBalance(),
        accountDb.getCreationTime()
    );
  }
}