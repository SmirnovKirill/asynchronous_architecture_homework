package asynchomework.accounting.service.domain;

import asynchomework.accounting.service.db.AccountingUserDb;
import java.time.OffsetDateTime;

public record AccountingUser(
    long id,
    String publicId,
    String name,
    AccountingUserRole role,
    OffsetDateTime creationTime
) {
  public static AccountingUser from(AccountingUserDb userDb) {
    return new AccountingUser(userDb.getUserId(), userDb.getUserPublicId(), userDb.getName(), userDb.getRole(), userDb.getCreationTime());
  }
}
