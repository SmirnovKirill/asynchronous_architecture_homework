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

  public TrackerUser getCurrentUser(String cookieWithUserId) {
    return TrackerUser.from(userDao.findById(Long.valueOf(cookieWithUserId)).orElseThrow());
  }

  public boolean canViewAllTasks(String cookieWithUserId) {
    return getCurrentUser(cookieWithUserId).role() == TrackerUserRole.ADMINISTRATOR
        || getCurrentUser(cookieWithUserId).role() == TrackerUserRole.MANAGER;
  }

  public boolean canViewOwnTasks(String cookieWithUserId) {
    return getCurrentUser(cookieWithUserId).role() == TrackerUserRole.EMPLOYEE;
  }

  public boolean canShuffleTasks(String cookieWithUserId) {
    return getCurrentUser(cookieWithUserId).role() == TrackerUserRole.ADMINISTRATOR
        || getCurrentUser(cookieWithUserId).role() == TrackerUserRole.MANAGER;
  }
}
