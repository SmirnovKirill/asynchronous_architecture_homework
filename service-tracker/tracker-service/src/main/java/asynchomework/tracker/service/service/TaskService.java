package asynchomework.tracker.service.service;

import asynchomework.tracker.messageapi.cud.ModifyTypeKafka;
import asynchomework.tracker.service.dao.TaskDao;
import asynchomework.tracker.service.dao.TrackerUserDao;
import asynchomework.tracker.service.db.TaskDb;
import asynchomework.tracker.service.db.TrackerUserDb;
import asynchomework.tracker.service.domain.CreateTask;
import asynchomework.tracker.service.domain.Task;
import asynchomework.tracker.service.domain.TaskStatus;
import asynchomework.tracker.service.domain.TrackerUser;
import asynchomework.tracker.service.kafka.KafkaProducer;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
  private final TaskDao taskDao;
  private final TrackerUserDao trackerUserDao;
  private final TaskPriceService taskPriceService;
  private final KafkaProducer kafkaProducer;

  public TaskService(TaskDao taskDao, TrackerUserDao trackerUserDao, TaskPriceService taskPriceService, KafkaProducer kafkaProducer) {
    this.taskDao = taskDao;
    this.trackerUserDao = trackerUserDao;
    this.taskPriceService = taskPriceService;
    this.kafkaProducer = kafkaProducer;
  }

  @Transactional
  public Task createTask(CreateTask createTask) {
    BigDecimal assignFee = taskPriceService.calculateAssignFee();
    BigDecimal resolvePrice = taskPriceService.calculateResolvePrice();

    TaskDb taskDb = new TaskDb(
        createTask.title(),
        createTask.description(),
        TaskStatus.IN_PROGRESS,
        trackerUserDao.getRandomAssignee(),
        assignFee,
        resolvePrice,
        OffsetDateTime.now()
    );
    Task task = Task.from(taskDao.save(taskDb));

    kafkaProducer.sendTaskCreatedOrModifiedCud(task, ModifyTypeKafka.CREATE);
    kafkaProducer.sendTaskCreated(task);
    kafkaProducer.sendTaskAssigned(task);

    return task;
  }

  @Transactional
  public List<Task> getAllTasks() {
    return taskDao.findAll().stream().map(Task::from).sorted(Comparator.comparing(Task::id)).toList();
  }

  @Transactional
  public List<Task> getUserTasks(TrackerUser user) {
    return taskDao.getUserTasks(user.id()).stream().map(Task::from).toList();
  }

  @Transactional
  public Optional<Task> getTask(long taskId) {
    return taskDao.findById(taskId).map(Task::from);
  }

  @Transactional
  public void resolveTask(long taskId) {
    taskDao.resolveTask(taskId);
    Task updatedTask = getTask(taskId).orElseThrow();
    kafkaProducer.sendTaskResolved(updatedTask);
    kafkaProducer.sendTaskCreatedOrModifiedCud(updatedTask, ModifyTypeKafka.UPDATE);
  }

  @Transactional
  public void removeTask(long taskId) {
    taskDao.deleteById(taskId);
    kafkaProducer.sendTaskDeletedCud(taskId);
  }

  public void scheduleShuffleTasks() {
    kafkaProducer.sendShuffleTasks();
  }

  @Transactional
  public void shuffleTasks() {
    List<Task> tasks = getAllTasks();
    for (Task task : tasks) {
      TrackerUserDb newAssignee = trackerUserDao.getRandomAssignee();
      taskDao.changeAssignee(task.id(), newAssignee.getUserId());

      Task updatedTask = getTask(task.id()).orElseThrow();
      kafkaProducer.sendTaskAssigned(updatedTask);
      kafkaProducer.sendTaskCreatedOrModifiedCud(updatedTask, ModifyTypeKafka.UPDATE);
    }
  }
}