package asynchomework.accounting.service.dao;

import asynchomework.accounting.service.db.AccountingUserDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AccountingUserDao extends SimpleJpaRepository<AccountingUserDb, Long> {
  private final EntityManager em;

  public AccountingUserDao(EntityManager em) {
    super(AccountingUserDb.class, em);
    this.em = em;
  }

  @Override
  public <S extends AccountingUserDb> S save(S entity) {
    em.persist(entity);
    return entity;
  }

  public <S extends AccountingUserDb> S update(S entity) {
    em.merge(entity);
    return entity;
  }

  public Optional<AccountingUserDb> getUserByPublicId(String publicId) {
    try {
      return Optional.of(
          em.createQuery("select user from AccountingUserDb user where userPublicId = :publicId", AccountingUserDb.class)
              .setParameter("publicId", publicId)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}

