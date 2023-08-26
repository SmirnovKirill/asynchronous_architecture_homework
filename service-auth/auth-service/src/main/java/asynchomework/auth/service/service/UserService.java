package asynchomework.auth.service.service;

import asynchomework.auth.service.dao.UserDao;
import asynchomework.auth.service.db.PopugUserDb;
import asynchomework.auth.service.domain.CreateUser;
import asynchomework.auth.service.domain.User;
import asynchomework.auth.service.kafka.KafkaProducer;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private final UserDao userDao;
  private final KafkaProducer kafkaProducer;

  public UserService(UserDao userDao, KafkaProducer kafkaProducer) {
    this.userDao = userDao;
    this.kafkaProducer = kafkaProducer;
  }

  @Transactional
  public User save(CreateUser user) {
    PopugUserDb popugUserDb = userDao.save(new PopugUserDb(user.name(), user.beakSize(), user.role(), OffsetDateTime.now()));
    User savedUser = User.from(popugUserDb);

    kafkaProducer.sendUserCreated(savedUser);
    kafkaProducer.sendUserCreatedCud(savedUser);

    return savedUser;
  }

  @Transactional
  public List<User> getAll() {
    return StreamSupport.stream(userDao.findAll().spliterator(), false).map(User::from).sorted(Comparator.comparing(User::id)).toList();
  }

  @Transactional
  public void removeUser(long userId) {
    userDao.deleteById(userId);

    kafkaProducer.sendUserDeletedCud(userId);
  }

  @Transactional
  public Optional<User> getByName(String name) {
    return userDao.findByName(name).map(User::from);
  }
}
