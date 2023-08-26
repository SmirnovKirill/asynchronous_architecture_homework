package asynchomework.eventapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.OffsetDateTime;

public record Event<T extends EventData>(
  String eventId,
  EventName eventName,
  OffsetDateTime eventTime,
  String producer,
  T data
){
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .registerModule(new JavaTimeModule());

  public String toJsonString() {
    try {
      return OBJECT_MAPPER.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T extends EventData> Event<T> eventFromString(String eventJsonString) {
    try {
      return OBJECT_MAPPER.readValue(eventJsonString,new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
