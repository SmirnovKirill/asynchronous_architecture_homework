package asynchomework.tracker.service.kafka;

import asynchomework.auth.messageapi.cud.UserModifiedKafkaCud;
import asynchomework.tracker.service.config.KafkaConfig;
import asynchomework.tracker.service.service.TaskService;
import asynchomework.tracker.service.service.TrackerUserService;
import com.fasterxml.jackson.core.JsonProcessingException;

public class KafkaListener {
  private final TrackerUserService trackerUserService;
  private final TaskService taskService;

  public KafkaListener(TrackerUserService trackerUserService, TaskService taskService) {
    this.trackerUserService = trackerUserService;
    this.taskService = taskService;
  }

  @org.springframework.kafka.annotation.KafkaListener(topics = "cud.user_modified")
  public void processCudUserModifiedMessage(String message) {
    UserModifiedKafkaCud userModifiedKafkaCud = convertMessage(message, UserModifiedKafkaCud.class);
    trackerUserService.processUserCud(userModifiedKafkaCud);
  }

  @org.springframework.kafka.annotation.KafkaListener(topics = "shuffle_tasks")
  public void shuffleTasks() {
    taskService.shuffleTasks();
  }

  private <T> T convertMessage(String messageText, Class<T> messageClass) {
    try {
      return KafkaConfig.OBJECT_MAPPER.readValue(messageText, messageClass);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
