package asynchomework.auth.service.kafka;

import asynchomework.auth.service.domain.User;
import asynchomework.eventapi.Event;
import asynchomework.eventapi.EventData;
import asynchomework.eventapi.SchemaValidator;
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

    validateAndSend(Topic.USER, user.publicId(), userCreatedEvent);
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

    validateAndSend(Topic.USER_STREAM, user.publicId(), userStreamEvent);
  }

  public void sendUserDeletedStream(String userPublicId) {
    UserStreamEvent userStreamEvent = new UserStreamEvent(
        StreamEventType.DELETE,
        userPublicId,
        null,
        0,
        null,
        null
    );

    validateAndSend(Topic.USER_STREAM, userPublicId, userStreamEvent);
  }

  private <T extends EventData> void validateAndSend(Topic topic, String key, T data) {
    Event<T> event = new Event<>(UUID.randomUUID().toString(), 1, data.getEventName(), OffsetDateTime.now(), "auth", data);
    SchemaValidator.validate(event);
    this.template.send(topic.getName(), key, event.toJsonString());
  }
}
