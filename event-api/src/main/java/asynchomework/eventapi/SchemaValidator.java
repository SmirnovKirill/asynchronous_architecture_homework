package asynchomework.eventapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public class SchemaValidator {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .registerModule(new JavaTimeModule());

  private static final JsonSchemaFactory SCHEMA_FACTORY = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);

  private static final Map<Pair<EventName, Integer>, JsonSchema> SCHEMAS = Map.of(
      Pair.of(EventName.USER_STREAM, 1), getSchemaForEvnet(EventName.USER_STREAM, 1),
      Pair.of(EventName.USER_CREATED, 1), getSchemaForEvnet(EventName.USER_CREATED, 1),
      Pair.of(EventName.TASK_STREAM, 1), getSchemaForEvnet(EventName.TASK_STREAM, 1),
      Pair.of(EventName.TASK_STREAM, 2), getSchemaForEvnet(EventName.TASK_STREAM, 2),
      Pair.of(EventName.TASK_ASSIGNED, 1), getSchemaForEvnet(EventName.TASK_ASSIGNED, 1),
      Pair.of(EventName.TASK_CREATED, 1), getSchemaForEvnet(EventName.TASK_CREATED, 1),
      Pair.of(EventName.TASK_RESOLVED, 1), getSchemaForEvnet(EventName.TASK_RESOLVED, 1)
  );

  private static JsonSchema getSchemaForEvnet(EventName eventName, int version) {
    return SCHEMA_FACTORY.getSchema(
        SchemaValidator.class.getResourceAsStream(
            "/schema/%s/v%d/%s.json".formatted(eventName.name().toLowerCase(), version, eventName.name().toLowerCase())
        )
    );
  }

  public static void validate(Event<?> event) {
    Set<ValidationMessage> validationMessages = SCHEMAS.get(Pair.of(event.data().getEventName(), event.eventVersion()))
        .validate(OBJECT_MAPPER.valueToTree(event.data()));
    if (!validationMessages.isEmpty()) {
      throw new IllegalStateException(
          "Got %d validation messages when validating %s".formatted(validationMessages.size(), event.data().getEventName())
      );
    }
  }

  public static void validate(String json, EventName eventName, int version) {
    Set<ValidationMessage> validationMessages;
    try {
      validationMessages = SCHEMAS.get(Pair.of(eventName, version)).validate(OBJECT_MAPPER.readTree(json));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    if (!validationMessages.isEmpty()) {
      throw new IllegalStateException("Got %d validation messages when validating %s".formatted(validationMessages.size(), eventName));
    }
  }
}
