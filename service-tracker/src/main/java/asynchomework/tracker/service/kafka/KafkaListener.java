package asynchomework.tracker.service.kafka;

import asynchomework.eventapi.Event;
import asynchomework.eventapi.user.UserStreamEvent;
import asynchomework.tracker.service.service.TaskService;
import asynchomework.tracker.service.service.TrackerUserService;

public class KafkaListener {
  private final TrackerUserService trackerUserService;
  private final TaskService taskService;

  public KafkaListener(TrackerUserService trackerUserService, TaskService taskService) {
    this.trackerUserService = trackerUserService;
    this.taskService = taskService;
  }

  @org.springframework.kafka.annotation.KafkaListener(topics = "user_stream")
  public void processUserStreamEvent(String eventString) {
    Event<UserStreamEvent> userStreamEvent = Event.eventFromString(eventString);
    trackerUserService.processUserStreamEvent(userStreamEvent.data());
  }

  @org.springframework.kafka.annotation.KafkaListener(topics = "shuffle_tasks")
  public void shuffleTasks() {
    taskService.shuffleTasks();
  }
}
