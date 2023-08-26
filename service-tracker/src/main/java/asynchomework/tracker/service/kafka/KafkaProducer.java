package asynchomework.tracker.service.kafka;

import asynchomework.eventapi.Event;
import asynchomework.eventapi.EventData;
import asynchomework.eventapi.SchemaValidator;
import asynchomework.eventapi.StreamEventType;
import asynchomework.eventapi.Topic;
import asynchomework.eventapi.task.TaskAssignedEvent;
import asynchomework.eventapi.task.TaskCreatedEvent;
import asynchomework.eventapi.task.TaskResolvedEvent;
import asynchomework.eventapi.task.TaskStatus;
import asynchomework.eventapi.task.TaskStreamEvent;
import asynchomework.tracker.service.domain.Task;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
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
    validateAndSend(Topic.TASK, task.publicId(), new TaskCreatedEvent(task.publicId(), task.assignee().id()), 1);
  }

  public void sendTaskAssigned(Task task) {
    validateAndSend(Topic.TASK, task.publicId(), new TaskAssignedEvent(task.publicId(), task.assignee().id()), 1);
  }

  public void sendTaskResolved(Task task) {
    validateAndSend(Topic.TASK, task.publicId(), new TaskResolvedEvent(task.publicId(), task.assignee().id()), 1);
  }

  public void sendTaskCreatedOrModifiedStream(Task task, StreamEventType eventType) {
    TaskStreamEvent taskStreamEvent = new TaskStreamEvent(
        eventType,
        task.publicId(),
        task.title(),
        task.jiraId(),
        task.description(),
        TaskStatus.valueOf(task.status().toString()),
        task.assignee().id(),
        task.assignFee(),
        task.resolvePrice(),
        task.creationTime()
    );

    int version = StringUtils.isBlank(task.jiraId()) ? 1 : 2;
    validateAndSend(Topic.TASK_STREAM, task.publicId(), taskStreamEvent, version);
  }

  public void sendTaskDeletedStream(String taskPublicId) {
    TaskStreamEvent taskStreamEvent = new TaskStreamEvent(
        StreamEventType.DELETE,
        taskPublicId,
        null,
        null,
        null,
        null,
        0,
        null,
        null,
        null
    );

    validateAndSend(Topic.TASK_STREAM, taskPublicId, taskStreamEvent, 1);
  }

  private <T extends EventData> void validateAndSend(Topic topic, String key, T data, int version) {
    Event<T> event = new Event<>(UUID.randomUUID().toString(), version, data.getEventName(), OffsetDateTime.now(), "tracker", data);
    SchemaValidator.validate(event);
    this.template.send(topic.getName(), key, event.toJsonString());
  }
}
