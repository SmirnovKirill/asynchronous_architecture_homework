package asynchomework.auth.service.kafka;

import asynchomework.auth.service.domain.User;
import asynchomework.eventapi.Event;
import asynchomework.eventapi.EventData;
import asynchomework.eventapi.EventName;
import asynchomework.eventapi.StreamEventType;
import asynchomework.eventapi.Topic;
import asynchomework.eventapi.user.UserCreatedEvent;
import asynchomework.eventapi.user.UserRole;
import asynchomework.eventapi.user.UserStreamEvent;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducer {
  private final KafkaTemplate<String, String> template;

  public KafkaProducer(KafkaTemplate<String, String> template) {
    this.template = template;
  }

  public void sendUserCreated(User user) {
    UserCreatedEvent userCreatedEvent = new UserCreatedEvent(
        user.publicId(),
        user.name(),
        user.beakSize(),
        UserRole.valueOf(user.role().toString()),
        user.creationTime()
    );

    this.template.send(Topic.USER.getName(), user.publicId(), createEvent(userCreatedEvent, EventName.USER_CREATED).toJsonString());
  }

  public void sendUserCreatedStream(User user) {
    UserStreamEvent userStreamEvent = new UserStreamEvent(
        StreamEventType.CREATE,
        user.publicId(),
        user.name(),
        user.beakSize(),
        UserRole.valueOf(user.role().toString()),
        user.creationTime()
    );

    this.template.send(Topic.USER_STREAM.getName(), user.publicId(), createEvent(userStreamEvent, EventName.USER_STREAM).toJsonString());
  }

  public void sendUserDeletedCud(String userPublicId) {
    UserStreamEvent userStreamEvent = new UserStreamEvent(
        StreamEventType.DELETE,
        userPublicId,
        null,
        0,
        null,
        null
    );

    this.template.send(Topic.USER_STREAM.getName(), userPublicId, createEvent(userStreamEvent, EventName.USER_STREAM).toJsonString());
  }

  private <T extends EventData> Event<T> createEvent(T data, EventName eventName) {
    return new Event<>(UUID.randomUUID().toString(), eventName, OffsetDateTime.now(), "auth", data);
  }
}
