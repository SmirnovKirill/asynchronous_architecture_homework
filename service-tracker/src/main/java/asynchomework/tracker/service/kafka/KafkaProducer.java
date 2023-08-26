package asynchomework.tracker.service.kafka;

import asynchomework.eventapi.Event;
import asynchomework.eventapi.EventData;
import asynchomework.eventapi.EventName;
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
    this.template.send(
        Topic.TASK.getName(),
        task.publicId(),
        createEvent(new TaskCreatedEvent(task.publicId(), task.assignee().id()), EventName.TASK_CREATED).toJsonString()
    );
  }

  public void sendTaskAssigned(Task task) {
    this.template.send(
        Topic.TASK.getName(),
        task.publicId(),
        createEvent(new TaskAssignedEvent(task.publicId(), task.assignee().id()), EventName.TASK_ASSIGNED).toJsonString()
    );
  }

  public void sendTaskResolved(Task task) {
    this.template.send(
        Topic.TASK.getName(),
        task.publicId(),
        createEvent(new TaskResolvedEvent(task.publicId(), task.assignee().id()), EventName.TASK_RESOLVED).toJsonString()
    );
  }

  public void sendTaskCreatedOrModifiedStream(Task task, StreamEventType eventType) {
    TaskStreamEvent taskStreamEvent = new TaskStreamEvent(
        eventType,
        task.publicId(),
        task.title(),
        task.description(),
        TaskStatus.valueOf(task.status().toString()),
        task.assignee().id(),
        task.assignFee(),
        task.resolvePrice(),
        task.creationTime()
    );

    this.template.send(Topic.TASK_STREAM.getName(), task.publicId(), createEvent(taskStreamEvent, EventName.TASK_STREAM).toJsonString());
  }

  public void sendTaskDeletedStream(String taskPublicId) {
    TaskStreamEvent taskStreamEvent = new TaskStreamEvent(
        StreamEventType.DELETE,
        taskPublicId,
        null,
        null,
        null,
        0,
        null,
        null,
        null
    );

    this.template.send(Topic.TASK_STREAM.getName(), taskPublicId, createEvent(taskStreamEvent, EventName.TASK_STREAM).toJsonString());
  }

  private <T extends EventData> Event<T> createEvent(T data, EventName eventName) {
    return new Event<>(UUID.randomUUID().toString(), eventName, OffsetDateTime.now(), "tracker", data);
  }
}
