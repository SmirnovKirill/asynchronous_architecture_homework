package asynchomework.auth.service.dao;

import asynchomework.auth.service.db.PopugUserDb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<PopugUserDb, Long> {
}
