package asynchomework.accounting.service.dao;

import asynchomework.accounting.service.db.AccountDb;
import asynchomework.accounting.service.domain.Account;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AccountDao extends CrudRepository<AccountDb, Long> {
  @Query("delete from AccountDb where user.userId = :userId")
  AccountDb deleteByUserId(long userId);

  @Query("select account from AccountDb account where account.user.userId = :userId")
  Optional<AccountDb> getByUser(long userId);

  @Modifying
  @Query("update AccountDb set balance = balance + :amount where accountId = :accountId")
  void depositMoney(long accountId, BigDecimal amount);

  @Modifying
  @Query("update AccountDb set balance = balance - :amount where accountId = :accountId")
  void withdrawMoney(long accountId, BigDecimal amount);
}
