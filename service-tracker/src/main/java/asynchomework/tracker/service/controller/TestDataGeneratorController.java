package asynchomework.tracker.service.controller;

import asynchomework.tracker.service.domain.CreateTask;
import asynchomework.tracker.service.domain.Task;
import asynchomework.tracker.service.service.TaskService;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestDataGeneratorController {
  private static final Random RANDOM = new Random();

  private final TaskService taskService;
  public TestDataGeneratorController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("/generate")
  public void generate() {
    List<Task> allTasks = taskService.getAllTasks();
    allTasks.forEach(taskService::removeTask);

    LocalDate today = LocalDate.now();
    for (int i = 45; i >= 0; i--) {
      LocalDate taskDay = today.minusDays(i);
      generateTasksForDay(taskDay, 1, 10);
      generateTasksForDay(taskDay, 2, 40);

      List<Task> allTasksForDay = taskService.getAllTasksCreatedAt(taskDay);
      for (Task taskForDay : allTasksForDay) {
        if (RANDOM.nextBoolean()) {
          taskService.resolveTask(taskForDay.id(), taskForDay.creationTime().plusMinutes(RANDOM.nextInt(120)));
        }
      }

      for (int j = 0; j < RANDOM.nextInt(2); j++) {
        taskService.scheduleShuffleTasks();
      }
    }
  }

  private void generateTasksForDay(LocalDate taskDay, int version, int limit) {
    for (int i = 1; i <= limit; i++) {
      int randomHours = RANDOM.nextInt(24);
      int randomMinutes = RANDOM.nextInt(60);
      int randomSeconds = RANDOM.nextInt(60);
      OffsetDateTime taskTime = taskDay.atTime(randomHours, randomMinutes, randomSeconds).atZone(ZoneId.systemDefault()).toOffsetDateTime();

      if (version == 1) {
        taskService.createTask(new CreateTask("task-%d".formatted(i), "description-%d".formatted(i), taskTime));
      } else if (version == 2) {
        taskService.createTask(new CreateTask("[POPUG-AI] - task-%d".formatted(i), "description-%d".formatted(i), taskTime));
      } else {
        throw new IllegalStateException("Unexpected version %d".formatted(version));
      }
    }
  }
}
