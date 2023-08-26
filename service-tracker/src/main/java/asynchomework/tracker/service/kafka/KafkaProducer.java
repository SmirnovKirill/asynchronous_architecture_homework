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
    this.template.send(
        Topic.TASK.getName(),
        task.publicId(),
        createEvent(new TaskCreatedEvent(task.publicId(), task.assignee().id()), 1, EventName.TASK_CREATED).toJsonString()
    );
  }

  public void sendTaskAssigned(Task task) {
    this.template.send(
        Topic.TASK.getName(),
        task.publicId(),
        createEvent(new TaskAssignedEvent(task.publicId(), task.assignee().id()), 1, EventName.TASK_ASSIGNED).toJsonString()
    );
  }

  public void sendTaskResolved(Task task) {
    this.template.send(
        Topic.TASK.getName(),
        task.publicId(),
        createEvent(new TaskResolvedEvent(task.publicId(), task.assignee().id()), 1, EventName.TASK_RESOLVED).toJsonString()
    );
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
    this.template.send(Topic.TASK_STREAM.getName(), task.publicId(), createEvent(taskStreamEvent, version, EventName.TASK_STREAM).toJsonString());
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

    this.template.send(Topic.TASK_STREAM.getName(), taskPublicId, createEvent(taskStreamEvent, 1, EventName.TASK_STREAM).toJsonString());
  }

  private <T extends EventData> Event<T> createEvent(T data, int version, EventName eventName) {
    return new Event<>(UUID.randomUUID().toString(), version, eventName, OffsetDateTime.now(), "tracker", data);
  }
}
