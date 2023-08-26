package asynchomework.tracker.service.dao;

import asynchomework.tracker.service.db.TrackerUserDb;
import asynchomework.tracker.service.domain.TrackerUserRole;
import jakarta.persistence.EntityManager;
import java.util.Set;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TrackerUserDao extends SimpleJpaRepository<TrackerUserDb, Long> {
  private static final Set<TrackerUserRole> ALLOWED_ASSIGNEE_ROLES = Set.of(TrackerUserRole.EMPLOYEE);

  private final EntityManager em;

  public TrackerUserDao(EntityManager em) {
    super(TrackerUserDb.class, em);
    this.em = em;
  }

  @Override
  public <S extends TrackerUserDb> S save(S entity) {
    em.persist(entity);
    return entity;
  }

  public <S extends TrackerUserDb> S update(S entity) {
    em.merge(entity);
    return entity;
  }

  public TrackerUserDb getRandomAssignee() {
    return (TrackerUserDb) em.createNativeQuery(
        "SELECT * FROM tracker_user WHERE user_role IN (:allowedAssigneeRoles) ORDER BY RANDOM() LIMIT 1",
            TrackerUserDb.class
        )
        .setParameter("allowedAssigneeRoles", ALLOWED_ASSIGNEE_ROLES.stream().map(Enum::name).toList())
        .getSingleResult();
  }
}

