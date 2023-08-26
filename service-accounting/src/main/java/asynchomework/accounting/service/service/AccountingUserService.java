package asynchomework.accounting.service.service;

import asynchomework.accounting.service.dao.AccountingUserDao;
import asynchomework.accounting.service.db.AccountDb;
import asynchomework.accounting.service.db.AccountingUserDb;
import asynchomework.accounting.service.domain.AccountingUser;
import asynchomework.accounting.service.domain.AccountingUserRole;
import asynchomework.eventapi.user.UserStreamEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountingUserService {
  private final AccountingUserDao userDao;
  private final AccountService accountService;
  private final AccountLogService accountLogService;

  public AccountingUserService(AccountingUserDao userDao, AccountService accountService, AccountLogService accountLogService) {
    this.userDao = userDao;
    this.accountService = accountService;
    this.accountLogService = accountLogService;
  }

  @Transactional
  public void processUserStreamEvent(UserStreamEvent userStreamEvent) {
    switch (userStreamEvent.eventType()) {
      case CREATE -> userDao.save(dbUserFrom(userStreamEvent));
      case UPDATE -> userDao.update(dbUserFrom(userStreamEvent));
      case DELETE -> {
        AccountingUserDb user = userDao.getUserByPublicId(userStreamEvent.userPublicId()).orElseThrow();
        AccountDb account = accountService.deleteAccount(user.getUserId());
        accountLogService.deleteForAccount(account.getAccountId());
        userDao.deleteById(user.getUserId());
      }
    }
  }

  private static AccountingUserDb dbUserFrom(UserStreamEvent userStreamEvent) {
    return new AccountingUserDb(
        userStreamEvent.userPublicId(),
        userStreamEvent.name(),
        AccountingUserRole.valueOf(userStreamEvent.role().name()),
        userStreamEvent.creationTime()
    );
  }
}
