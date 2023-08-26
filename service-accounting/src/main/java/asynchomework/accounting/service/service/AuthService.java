package asynchomework.accounting.service.service;


import asynchomework.accounting.service.dao.AccountingUserDao;
import asynchomework.accounting.service.domain.AccountingUser;
import asynchomework.accounting.service.domain.AccountingUserRole;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final AccountingUserDao userDao;

  public AuthService(AccountingUserDao userDao) {
    this.userDao = userDao;
  }

  public AccountingUser getCurrentUser(String userPublicId) {
    return AccountingUser.from(userDao.getUserByPublicId(userPublicId).orElseThrow());
  }

  public boolean canViewPersonalAccountInfo(String userPublicId) {
    return getCurrentUser(userPublicId).role() == AccountingUserRole.EMPLOYEE;
  }

  public boolean canViewAllAccountsSummary(String userPublicId) {
    return getCurrentUser(userPublicId).role() == AccountingUserRole.ADMINISTRATOR
           || getCurrentUser(userPublicId).role() == AccountingUserRole.MANAGER;
  }
}
