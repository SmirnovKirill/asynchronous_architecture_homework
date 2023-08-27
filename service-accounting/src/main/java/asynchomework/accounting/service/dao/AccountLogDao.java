package asynchomework.accounting.service.dao;

import asynchomework.accounting.service.db.AccountLogDb;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AccountLogDao extends CrudRepository<AccountLogDb, Long> {
  @Query("delete from AccountLogDb where account.accountId = :accountId")
  void deleteByAccountId(long accountId);

  @Query("from AccountLogDb where account.accountId = :accountId order by operationTime asc")
  List<AccountLogDb> getByAccountId(long accountId);

  @Query("from AccountLogDb where operationTime >= :dateFrom and operationTime <= :dateTo order by operationTime asc")
  List<AccountLogDb> getForDate(OffsetDateTime dateFrom, OffsetDateTime dateTo);
}
