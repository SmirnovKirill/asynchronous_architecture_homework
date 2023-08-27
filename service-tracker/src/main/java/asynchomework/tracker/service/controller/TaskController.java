package asynchomework.tracker.service.controller;

import asynchomework.tracker.service.domain.AuthException;
import asynchomework.tracker.service.domain.CreateTask;
import asynchomework.tracker.service.domain.Task;
import asynchomework.tracker.service.domain.TaskStatus;
import asynchomework.tracker.service.service.AuthService;
import asynchomework.tracker.service.service.TaskService;
import java.time.OffsetDateTime;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/task",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class TaskController {
  private final AuthService authService;
  private final TaskService taskService;

  public TaskController(AuthService authService, TaskService taskService) {
    this.authService = authService;
    this.taskService = taskService;
  }

  @PostMapping
  public Task createTask(@RequestBody CreateTask createTask) {
    return taskService.createTask(createTask);
  }

  @GetMapping("all")
  public List<Task> getAllTasks(@CookieValue(value = "userPublicId", required = false) String userPublicId) {
    if (StringUtils.isBlank(userPublicId) || !authService.canViewAllTasks(userPublicId)) {
      throw new AuthException("Incorrect role for viewing all tasks");
    }

    return taskService.getAllTasks();
  }

  @GetMapping("current_user")
  public List<Task> getCurrentUserTasks(@CookieValue(value = "userPublicId", required = false) String userPublicId) {
    if (StringUtils.isBlank(userPublicId) || !authService.canViewOwnTasks(userPublicId)) {
      throw new AuthException("Incorrect role for viewing current user tasks");
    }

    return taskService.getUserTasks(authService.getCurrentUser(userPublicId));
  }

  @PostMapping("resolve/{taskId}")
  public void resolveTask(@CookieValue(value = "userPublicId", required = false) String userPublicId, @PathVariable long taskId) {
    if (StringUtils.isBlank(userPublicId)  || !authService.canViewOwnTasks(userPublicId)) {
      throw new AuthException("Incorrect role for resolving tasks");
    }

    Task task = taskService.getTask(taskId).orElse(null);
    if (task == null) {
      throw new IllegalStateException("Task %d not found".formatted(taskId));
    }

    if (task.status() != TaskStatus.IN_PROGRESS) {
      throw new IllegalStateException("Can't resolve task that is not in progress");
    }

    if (task.assignee().id() != authService.getCurrentUser(userPublicId).id()) {
      throw new IllegalStateException("User can't resolve other user's task");
    }

    taskService.resolveTask(taskId, OffsetDateTime.now());
  }

  @PostMapping("shuffle")
  public void shuffle(@CookieValue(value = "userPublicId", required = false) String userPublicId) {
    if (StringUtils.isBlank(userPublicId)  || !authService.canShuffleTasks(userPublicId)) {
      throw new AuthException("Incorrect role for shuffling tasks");
    }

    taskService.scheduleShuffleTasks();
  }
}
