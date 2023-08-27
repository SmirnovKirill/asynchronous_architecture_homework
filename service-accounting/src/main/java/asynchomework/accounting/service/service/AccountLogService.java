package asynchomework.accounting.service.service;

import asynchomework.accounting.service.dao.AccountLogDao;
import org.springframework.stereotype.Service;

@Service
public class AccountLogService {
  private final AccountLogDao accountLogDao;

  public AccountLogService(AccountLogDao accountLogDao) {
    this.accountLogDao = accountLogDao;
  }

  public void deleteForAccount(long accountId) {
    accountLogDao.deleteByAccountId(accountId);
  }
}
