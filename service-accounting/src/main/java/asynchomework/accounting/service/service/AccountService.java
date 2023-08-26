package asynchomework.accounting.service.service;

import asynchomework.accounting.service.dao.AccountDao;
import asynchomework.accounting.service.db.AccountDb;
import asynchomework.accounting.service.db.AccountingUserDb;
import asynchomework.accounting.service.domain.Account;
import asynchomework.accounting.service.kafka.KafkaProducer;
import asynchomework.eventapi.StreamEventType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
  private final AccountDao accountDao;
  private final KafkaProducer kafkaProducer;

  public AccountService(AccountDao accountDao, KafkaProducer kafkaProducer) {
    this.accountDao = accountDao;
    this.kafkaProducer = kafkaProducer;
  }

  public AccountDb deleteAccount(long userId) {
    return accountDao.deleteByUserId(userId);
  }

  public void createAccount(AccountingUserDb userDb) {
    AccountDb accountDb = new AccountDb(
        UUID.randomUUID().toString(),
        userDb,
        BigDecimal.ZERO,
        OffsetDateTime.now()
    );
    accountDao.save(accountDb);
    kafkaProducer.sendAccountCreatedOrModifiedStream(Account.from(accountDb), StreamEventType.CREATE);
  }
}
