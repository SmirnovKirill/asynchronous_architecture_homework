package asynchomework.tracker.service.service;

import asynchomework.eventapi.user.UserStreamEvent;
import asynchomework.tracker.service.dao.TaskDao;
import asynchomework.tracker.service.dao.TrackerUserDao;
import asynchomework.tracker.service.db.TrackerUserDb;
import asynchomework.tracker.service.domain.TrackerUserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrackerUserService {
  private final TrackerUserDao userDao;
  private final TaskDao taskDao;

  public TrackerUserService(TrackerUserDao userDao, TaskDao taskDao) {
    this.userDao = userDao;
    this.taskDao = taskDao;
  }

  @Transactional
  public void processUserStreamEvent(UserStreamEvent userStreamEvent) {
    switch (userStreamEvent.eventType()) {
      case CREATE -> userDao.save(dbUserFrom(userStreamEvent));
      case UPDATE -> userDao.update(dbUserFrom(userStreamEvent));
      case DELETE -> {
        TrackerUserDb user = userDao.getUserByPublicId(userStreamEvent.userPublicId()).orElseThrow();
        taskDao.deleteUserTasks(user.getUserId());
        userDao.deleteById(user.getUserId());
      }
    }
  }

  private static TrackerUserDb dbUserFrom(UserStreamEvent userStreamEvent) {
    return new TrackerUserDb(
        userStreamEvent.userPublicId(),
        userStreamEvent.name(),
        TrackerUserRole.valueOf(userStreamEvent.role().name()),
        userStreamEvent.creationTime()
    );
  }
}
