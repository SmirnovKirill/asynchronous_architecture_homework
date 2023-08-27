package asynchomework.tracker.service.service;

import asynchomework.eventapi.StreamEventType;
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
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        UUID.randomUUID().toString(),
        createTask.title(),
        createTask.description(),
        TaskStatus.IN_PROGRESS,
        trackerUserDao.getRandomAssignee(),
        assignFee,
        resolvePrice,
        Optional.ofNullable(createTask.creationTime()).orElse(OffsetDateTime.now())
    );
    Task task = Task.from(taskDao.save(taskDb));

    kafkaProducer.sendTaskCreatedOrModifiedStream(task, StreamEventType.CREATE);
    kafkaProducer.sendTaskCreated(task);
    kafkaProducer.sendTaskAssigned(task);

    return task;
  }

  @Transactional
  public List<Task> getAllTasks() {
    return taskDao.findAll().stream().map(Task::from).sorted(Comparator.comparing(Task::id)).toList();
  }

  @Transactional
  public List<Task> getAllNotResolvedTasks() {
    return getAllTasks().stream()
        .filter(task -> task.status() == TaskStatus.IN_PROGRESS)
        .toList();
  }

  @Transactional
  public List<Task> getAllTasksCreatedAt(LocalDate creationDay) {
    return getAllTasks().stream()
        .filter(task -> task.creationTime().toLocalDate().equals(creationDay))
        .toList();
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
  public void resolveTask(long taskId, OffsetDateTime resolveTime) {
    taskDao.resolveTask(taskId);
    Task updatedTask = getTask(taskId).orElseThrow();
    kafkaProducer.sendTaskResolved(updatedTask, resolveTime);
    kafkaProducer.sendTaskCreatedOrModifiedStream(updatedTask, StreamEventType.UPDATE);
  }

  @Transactional
  public void removeTask(Task task) {
    taskDao.deleteById(task.id());
    kafkaProducer.sendTaskDeletedStream(task.publicId());
  }

  public void scheduleShuffleTasks() {
    kafkaProducer.sendShuffleTasks();
  }

  @Transactional
  public void shuffleTasks() {
    List<Task> tasks = getAllNotResolvedTasks();
    for (Task task : tasks) {
      TrackerUserDb newAssignee = trackerUserDao.getRandomAssignee();
      taskDao.changeAssignee(task.id(), newAssignee.getUserId());

      Task updatedTask = getTask(task.id()).orElseThrow();
      kafkaProducer.sendTaskAssigned(updatedTask);
      kafkaProducer.sendTaskCreatedOrModifiedStream(updatedTask, StreamEventType.UPDATE);
    }
  }
}