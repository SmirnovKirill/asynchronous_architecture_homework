package asynchomework.tracker.service.controller;

import asynchomework.tracker.service.domain.CreateTask;
import asynchomework.tracker.service.domain.Task;
import asynchomework.tracker.service.service.TaskService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestDataGeneratorController {
  private final TaskService taskService;
  public TestDataGeneratorController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("/generate")
  public void generate() {
    List<Task> allTasks = taskService.getAllTasks();
    allTasks.stream().map(Task::id).forEach(taskService::removeTask);

    for (int i = 1; i <= 100; i++) {
      taskService.createTask(new CreateTask("task-%d".formatted(i), "description-%d".formatted(i)));
    }
  }
}
