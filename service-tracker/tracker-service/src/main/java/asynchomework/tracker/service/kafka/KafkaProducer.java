package asynchomework.tracker.service.kafka;

import asynchomework.tracker.messageapi.TaskStatusKafka;
import asynchomework.tracker.messageapi.business.TaskAssignedKafka;
import asynchomework.tracker.messageapi.business.TaskCreatedKafka;
import asynchomework.tracker.messageapi.business.TaskResolvedKafka;
import asynchomework.tracker.messageapi.cud.ModifyTypeKafka;
import asynchomework.tracker.messageapi.cud.TaskModifiedKafkaCud;
import asynchomework.tracker.service.config.KafkaConfig;
import asynchomework.tracker.service.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducer {
  private final KafkaTemplate<String, String> template;

  public KafkaProducer(KafkaTemplate<String, String> template) {
    this.template = template;
  }

  public void sendShuffleTasks() {
    this.template.send("shuffle_tasks", "");
  }

  public void sendTaskCreated(Task task) {
    this.template.send("task_created", messageAsString(new TaskCreatedKafka(task.id(), task.assignee().id())));
  }

  public void sendTaskAssigned(Task task) {
    this.template.send("task_assigned", messageAsString(new TaskAssignedKafka(task.id(), task.assignee().id())));
  }

  public void sendTaskResolved(Task task) {
    this.template.send("task_resolved", messageAsString(new TaskResolvedKafka(task.id(), task.assignee().id())));
  }

  public void sendTaskCreatedOrModifiedCud(Task task, ModifyTypeKafka modifyType) {
    TaskModifiedKafkaCud taskModifiedKafkaCud = new TaskModifiedKafkaCud(
        modifyType,
        task.id(),
        task.title(),
        task.description(),
        TaskStatusKafka.valueOf(task.status().toString()),
        task.assignee().id(),
        task.assignFee(),
        task.resolvePrice(),
        task.creationTime()
    );

    this.template.send("cud.task_modified", String.valueOf(task.id()), messageAsString(taskModifiedKafkaCud));
  }

  public void sendTaskDeletedCud(long taskId) {
    TaskModifiedKafkaCud taskModifiedKafkaCud = new TaskModifiedKafkaCud(
        ModifyTypeKafka.DELETE,
        taskId,
        null,
        null,
        null,
        0,
        null,
        null,
        null
    );

    this.template.send("cud.task_modified", String.valueOf(taskId), messageAsString(taskModifiedKafkaCud));
  }

  private static String messageAsString(Object message) {
    try {
      return KafkaConfig.OBJECT_MAPPER.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
