package asynchomework.accounting.service.service;

import asynchomework.accounting.service.dao.AccountDao;
import asynchomework.accounting.service.dao.AccountLogDao;
import asynchomework.accounting.service.dao.AccountingTaskDao;
import asynchomework.accounting.service.dao.AccountingUserDao;
import asynchomework.accounting.service.db.AccountDb;
import asynchomework.accounting.service.db.AccountLogDb;
import asynchomework.accounting.service.db.AccountingTaskDb;
import asynchomework.accounting.service.db.AccountingUserDb;
import asynchomework.accounting.service.domain.Account;
import asynchomework.accounting.service.domain.AccountLog;
import asynchomework.accounting.service.domain.AccountingUser;
import asynchomework.accounting.service.domain.AllAccountsSummary;
import asynchomework.accounting.service.domain.AllAccountsSummaryDay;
import asynchomework.accounting.service.domain.LogExtraIds;
import asynchomework.accounting.service.domain.OperationType;
import asynchomework.accounting.service.domain.PersonalAccountInfo;
import asynchomework.accounting.service.kafka.KafkaProducer;
import asynchomework.eventapi.StreamEventType;
import asynchomework.eventapi.account.AccountOperationType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountingService {
  private final AccountingUserDao accountingUserDao;
  private final AccountingTaskDao accountingTaskDao;
  private final AccountDao accountDao;
  private final AccountLogDao accountLogDao;
  private final AccountService accountService;
  private final KafkaProducer kafkaProducer;

  public AccountingService(
      AccountingUserDao accountingUserDao,
      AccountingTaskDao accountingTaskDao,
      AccountDao accountDao,
      AccountLogDao accountLogDao,
      AccountService accountService,
      KafkaProducer kafkaProducer
  ) {
    this.accountingUserDao = accountingUserDao;
    this.accountingTaskDao = accountingTaskDao;
    this.accountDao = accountDao;
    this.accountLogDao = accountLogDao;
    this.accountService = accountService;
    this.kafkaProducer = kafkaProducer;
  }

  @Transactional
  public void createAccount(String userPublicId) {
    AccountingUserDb user = accountingUserDao.getUserByPublicId(userPublicId).orElseThrow();
    accountService.createAccount(user);
  }

  @Transactional
  public void depositMoneyForTask(String taskPublicId, OffsetDateTime creationTime) {
    addLogForTask(taskPublicId, OperationType.DEPOSIT, creationTime);
  }

  @Transactional
  public void withdrawMoneyForTask(String taskPublicId, OffsetDateTime withdrawTime) {
    addLogForTask(taskPublicId, OperationType.WITHDRAW, withdrawTime);
  }

  private void addLogForTask(String taskPublicId, OperationType operationType, OffsetDateTime operationTime) {
    AccountingTaskDb task = accountingTaskDao.getTaskByTaskPublicId(taskPublicId).orElseThrow();
    AccountingUserDb assignee = accountingUserDao.findById(task.getAssignee().getUserId()).orElseThrow();
    AccountDb account = accountDao.getByUser(assignee.getUserId()).orElseThrow();

    BigDecimal amount;
    AccountOperationType accountOperationType;
    switch (operationType) {
      case DEPOSIT -> {
        accountDao.depositMoney(account.getAccountId(), task.getResolvePrice());
        amount = task.getResolvePrice();
        accountOperationType = AccountOperationType.DEPOSIT;
      }
      case WITHDRAW -> {
        accountDao.withdrawMoney(account.getAccountId(), task.getAssignFee());
        amount = task.getAssignFee();
        accountOperationType = AccountOperationType.WITHDRAW;
      }
      default -> throw new IllegalStateException("Unexpected operation %s".formatted(operationType));
    }

    AccountLogDb accountLogDb = new AccountLogDb(account, operationType, amount, operationTime, new LogExtraIds(task.getTaskId()));
    accountLogDao.save(accountLogDb);

    AccountDb updatedAccount = accountDao.getByUser(assignee.getUserId()).orElseThrow();
    kafkaProducer.sendAccountCreatedOrModifiedStream(Account.from(updatedAccount), StreamEventType.UPDATE);
    kafkaProducer.sendAccountBalanceChangedStream(updatedAccount.getAccountPublicId(), amount, accountOperationType);
  }

  public PersonalAccountInfo getPersonalAccountInfo(AccountingUser user) {
    AccountDb accountDb = accountDao.getByUser(user.id()).orElseThrow();
    List<AccountLogDb> accountLogsDb = accountLogDao.getByAccountId(accountDb.getAccountId());
    List<AccountLog> accountLogs = accountLogsDb.stream()
        .map(accountLog -> AccountLog.from(accountLog, getTaskFromLog(accountLog)))
        .toList();

    return new PersonalAccountInfo(Account.from(accountDb), accountLogs);
  }

  private AccountingTaskDb getTaskFromLog(AccountLogDb accountLog) {
    if (accountLog.getExtraIds() == null || accountLog.getExtraIds().taskId() == null) {
      return null;
    }

    return accountingTaskDao.findById(accountLog.getExtraIds().taskId()).orElseThrow();
  }

  public AllAccountsSummary getAllAccountsSummary() {
    List<AllAccountsSummaryDay> summaryDays = new ArrayList<>();

    LocalDate today = LocalDate.now();
    for (int i = 0; i < 30; i++) {
      LocalDate date = today.minusDays(i);
      OffsetDateTime dateStart = date.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
      OffsetDateTime dateEnd = dateStart.plusDays(1).minusSeconds(1);

      List<AccountLogDb> logs = accountLogDao.getForDate(dateStart, dateEnd);
      BigDecimal withdraw = logs.stream()
          .filter(log -> log.getOperationType() == OperationType.WITHDRAW)
          .map(AccountLogDb::getAmount)
          .reduce(BigDecimal.ZERO, BigDecimal::add);
      BigDecimal deposit = logs.stream()
          .filter(log -> log.getOperationType() == OperationType.DEPOSIT)
          .map(AccountLogDb::getAmount)
          .reduce(BigDecimal.ZERO, BigDecimal::add);
      summaryDays.add(new AllAccountsSummaryDay(date, deposit.subtract(withdraw)));
    }

    return new AllAccountsSummary(summaryDays);
  }
}
