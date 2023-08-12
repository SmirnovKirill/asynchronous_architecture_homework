package asynchomework.auth.service.kafka;

import asynchomework.auth.messageapi.UserRoleKafka;
import asynchomework.auth.messageapi.business.UserCreatedKafka;
import asynchomework.auth.messageapi.cud.ModifyTypeKafka;
import asynchomework.auth.messageapi.cud.UserModifiedKafkaCud;
import asynchomework.auth.service.config.KafkaConfig;
import asynchomework.auth.service.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducer {
  private final KafkaTemplate<String, String> template;

  public KafkaProducer(KafkaTemplate<String, String> template) {
    this.template = template;
  }

  public void sendUserCreated(User user) {
    UserCreatedKafka userCreatedKafka = new UserCreatedKafka(
        user.id(),
        user.name(),
        user.beakSize(),
        UserRoleKafka.valueOf(user.role().toString()),
        user.creationTime()
    );

    this.template.send("user_created", String.valueOf(user.id()), messageAsString(userCreatedKafka));
  }

  public void sendUserCreatedCud(User user) {
    UserModifiedKafkaCud userModifiedKafkaCud = new UserModifiedKafkaCud(
        ModifyTypeKafka.CREATE,
        user.id(),
        user.name(),
        user.beakSize(),
        UserRoleKafka.valueOf(user.role().toString()),
        user.creationTime()
    );

    this.template.send("cud.user_modified", String.valueOf(user.id()), messageAsString(userModifiedKafkaCud));
  }

  public void sendUserDeletedCud(long userId) {
    UserModifiedKafkaCud userModifiedKafkaCud = new UserModifiedKafkaCud(
        ModifyTypeKafka.DELETE,
        userId,
        null,
        0,
        null,
        null
    );

    this.template.send("cud.user_modified", String.valueOf(userId),messageAsString(userModifiedKafkaCud));
  }

  private static String messageAsString(Object message) {
    try {
      return KafkaConfig.OBJECT_MAPPER.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
