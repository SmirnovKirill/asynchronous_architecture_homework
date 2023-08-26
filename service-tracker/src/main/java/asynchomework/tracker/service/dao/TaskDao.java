package asynchomework.tracker.service.dao;

import asynchomework.tracker.service.db.TaskDb;
import asynchomework.tracker.service.domain.TaskStatus;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TaskDao extends SimpleJpaRepository<TaskDb, Long> {
  private final EntityManager em;

  public TaskDao(EntityManager em) {
    super(TaskDb.class, em);
    this.em = em;
  }

  public List<TaskDb> getUserTasks(long userId) {
    return em.createQuery(
        "select task from TaskDb task where task.assignee.id = :userId order by id",
        TaskDb.class
    )
        .setParameter("userId", userId)
        .getResultList();
  }

  public void resolveTask(long taskId) {
    em.createQuery("update TaskDb set status = :statusResolved where taskId = :taskId")
        .setParameter("statusResolved", TaskStatus.DONE)
        .setParameter("taskId", taskId)
        .executeUpdate();
  }

  public void deleteUserTasks(long userId) {
    em.createQuery("delete from TaskDb where assignee.id = :userId")
        .setParameter("userId", userId)
        .executeUpdate();
  }

  public void changeAssignee(long taskId, long newAssigneeId) {
    em.createQuery("update TaskDb set assignee.id = :newAssigneeId where id = :taskId")
        .setParameter("newAssigneeId", newAssigneeId)
        .setParameter("taskId", taskId)
        .executeUpdate();
  }
}