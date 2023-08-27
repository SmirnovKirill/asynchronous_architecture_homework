package asynchomework.eventapi;

import asynchomework.eventapi.account.AccountBalanceChangedEvent;
import asynchomework.eventapi.account.AccountStreamEvent;
import asynchomework.eventapi.task.TaskAssignedEvent;
import asynchomework.eventapi.task.TaskCreatedEvent;
import asynchomework.eventapi.task.TaskResolvedEvent;
import asynchomework.eventapi.task.TaskStreamEvent;
import asynchomework.eventapi.user.UserCreatedEvent;
import asynchomework.eventapi.user.UserStreamEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;

public class SchemaGenerator {
  private static final com.github.victools.jsonschema.generator.SchemaGenerator GENERATOR;

  static {
    SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
    SchemaGeneratorConfig config = configBuilder.with(Option.NULLABLE_FIELDS_BY_DEFAULT).build();
    GENERATOR = new com.github.victools.jsonschema.generator.SchemaGenerator(config);
  }

  public static void main(String[] args) {
    JsonNode taskStreamSchema = GENERATOR.generateSchema(Event.class, TaskStreamEvent.class);
    JsonNode taskCreatedSchema = GENERATOR.generateSchema(Event.class, TaskCreatedEvent.class);
    JsonNode taskAssignedSchema = GENERATOR.generateSchema(Event.class, TaskAssignedEvent.class);
    JsonNode taskResolvedSchema = GENERATOR.generateSchema(Event.class, TaskResolvedEvent.class);

    JsonNode userStreamSchema = GENERATOR.generateSchema(Event.class, UserStreamEvent.class);
    JsonNode userCreatedSchema = GENERATOR.generateSchema(Event.class, UserCreatedEvent.class);

    JsonNode accountStreamSchema = GENERATOR.generateSchema(Event.class, AccountStreamEvent.class);
    JsonNode accountBalanceChangedSchema = GENERATOR.generateSchema(Event.class, AccountBalanceChangedEvent.class);


    writeToFile(taskStreamSchema, EventName.TASK_STREAM.name().toLowerCase(), 2);
    writeToFile(taskCreatedSchema, EventName.TASK_CREATED.name().toLowerCase(), 1);
    writeToFile(taskAssignedSchema, EventName.TASK_ASSIGNED.name().toLowerCase(), 1);
    writeToFile(taskResolvedSchema, EventName.TASK_RESOLVED.name().toLowerCase(), 1);
    writeToFile(userStreamSchema, EventName.USER_STREAM.name().toLowerCase(), 1);
    writeToFile(userCreatedSchema, EventName.USER_CREATED.name().toLowerCase(), 1);
    writeToFile(accountStreamSchema, EventName.ACCOUNT_STREAM.name().toLowerCase(), 1);
    writeToFile(accountBalanceChangedSchema, EventName.ACCOUNT_BALANCE_CHANGED.name().toLowerCase(), 1);
  }

  private static void writeToFile(JsonNode schema, String event, int version) {
    URL rootFolder = SchemaGenerator.class.getResource("/");
    if (rootFolder == null) {
      throw new IllegalStateException("No root folder!");
    }

    File file = Paths.get(rootFolder.getPath(), "schema", event, "v%d".formatted(version), "%s.json".formatted(event)).toFile();
    try {
      FileUtils.write(file, schema.toPrettyString(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
