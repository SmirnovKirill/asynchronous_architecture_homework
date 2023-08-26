package asynchomework.tracker.service.service;

import asynchomework.auth.messageapi.cud.UserModifiedKafkaCud;
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
  public void processUserCud(UserModifiedKafkaCud userModifiedKafkaCud) {
    switch (userModifiedKafkaCud.modifyType()) {
      case CREATE -> userDao.save(dbUserFrom(userModifiedKafkaCud));
      case UPDATE -> userDao.update(dbUserFrom(userModifiedKafkaCud));
      case DELETE -> {
        taskDao.deleteUserTasks(userModifiedKafkaCud.id());
        userDao.deleteById(userModifiedKafkaCud.id());
      }
    }
  }

  private static TrackerUserDb dbUserFrom(UserModifiedKafkaCud userModifiedKafkaCud) {
    return new TrackerUserDb(
        userModifiedKafkaCud.id(),
        userModifiedKafkaCud.name(),
        TrackerUserRole.valueOf(userModifiedKafkaCud.role().name()),
        userModifiedKafkaCud.creationTime()
    );
  }
}
