package asynchomework.accounting.service.dao;

import asynchomework.accounting.service.db.AccountingTaskDb;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountingTaskDao extends CrudRepository<AccountingTaskDb, Long> {
  Optional<AccountingTaskDb> getTaskByTaskPublicId(String publicId);
}

