package asynchomework.auth.service.service;

import asynchomework.auth.service.dao.UserDao;
import asynchomework.auth.service.db.PopugUserDb;
import asynchomework.auth.service.domain.CreateUser;
import asynchomework.auth.service.domain.User;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private final UserDao userDao;

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  @Transactional
  public User save(CreateUser user) {
    PopugUserDb popugUserDb = userDao.save(new PopugUserDb(user.name(), user.beakSize(), user.role(), OffsetDateTime.now()));
    return userFrom(popugUserDb);
  }

  @Transactional
  public Optional<User> get(long userId) {
    return userDao.findById(userId).map(UserService::userFrom);
  }

  private static User userFrom(PopugUserDb popugUserDb) {
    return new User(popugUserDb.getUserId(), popugUserDb.getName(), popugUserDb.getBeakSize(), popugUserDb.getRole(), popugUserDb.getCreationTime());
  }
}
