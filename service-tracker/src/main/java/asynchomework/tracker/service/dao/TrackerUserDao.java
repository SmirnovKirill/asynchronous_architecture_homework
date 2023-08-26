package asynchomework.tracker.service.dao;

import asynchomework.tracker.service.db.TaskDb;
import asynchomework.tracker.service.db.TrackerUserDb;
import asynchomework.tracker.service.domain.TrackerUserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;
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

  public Optional<TrackerUserDb> getUserByPublicId(String publicId) {
    try {
      return Optional.of(
          em.createQuery("select user from TrackerUserDb user where userPublicId = :publicId", TrackerUserDb.class)
              .setParameter("publicId", publicId)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
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

