package asynchomework.tracker.service.service;


import asynchomework.tracker.service.dao.TrackerUserDao;
import asynchomework.tracker.service.domain.TrackerUser;
import asynchomework.tracker.service.domain.TrackerUserRole;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final TrackerUserDao userDao;

  public AuthService(TrackerUserDao userDao) {
    this.userDao = userDao;
  }

  public TrackerUser getCurrentUser(String userPublicId) {
    return TrackerUser.from(userDao.getUserByPublicId(userPublicId).orElseThrow());
  }

  public boolean canViewAllTasks(String userPublicId) {
    return getCurrentUser(userPublicId).role() == TrackerUserRole.ADMINISTRATOR
        || getCurrentUser(userPublicId).role() == TrackerUserRole.MANAGER;
  }

  public boolean canViewOwnTasks(String userPublicId) {
    return getCurrentUser(userPublicId).role() == TrackerUserRole.EMPLOYEE;
  }

  public boolean canShuffleTasks(String userPublicId) {
    return getCurrentUser(userPublicId).role() == TrackerUserRole.ADMINISTRATOR
        || getCurrentUser(userPublicId).role() == TrackerUserRole.MANAGER;
  }
}
