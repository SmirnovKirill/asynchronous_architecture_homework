package asynchomework.accounting.service.service;

import asynchomework.accounting.service.dao.AccountingTaskDao;
import asynchomework.accounting.service.dao.AccountingUserDao;
import asynchomework.accounting.service.db.AccountingTaskDb;
import asynchomework.eventapi.task.TaskStreamEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountingTaskService {
  private final AccountingTaskDao taskDao;
  private final AccountingUserDao userDao;

  public AccountingTaskService(AccountingTaskDao taskDao, AccountingUserDao userDao) {
    this.taskDao = taskDao;
    this.userDao = userDao;
  }

  @Transactional
  public void processTaskStreamEvent(TaskStreamEvent taskStreamEvent) {
    switch (taskStreamEvent.eventType()) {
      case CREATE -> taskDao.save(dbTaskFrom(taskStreamEvent));
      case UPDATE -> {
        AccountingTaskDb existingTask = taskDao.getTaskByTaskPublicId(taskStreamEvent.taskPublicId()).orElseThrow();
        AccountingTaskDb taskToSave = dbTaskFrom(taskStreamEvent);
        taskToSave.setTaskId(existingTask.getTaskId());
        taskDao.save(taskToSave);
      }
      case DELETE -> {
        AccountingTaskDb task = taskDao.getTaskByTaskPublicId(taskStreamEvent.taskPublicId()).orElseThrow();
        taskDao.deleteById(task.getTaskId());
      }
    }
  }

  private AccountingTaskDb dbTaskFrom(TaskStreamEvent userStreamEvent) {
    return new AccountingTaskDb(
        userStreamEvent.taskPublicId(),
        userStreamEvent.title(),
        userDao.getUserByPublicId(userStreamEvent.assigneePublicId()).orElseThrow(),
        userStreamEvent.assignFee(),
        userStreamEvent.resolvePrice(),
        userStreamEvent.creationTime()
    );
  }
}
