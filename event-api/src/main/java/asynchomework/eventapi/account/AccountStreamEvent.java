package asynchomework.eventapi.account;

import asynchomework.eventapi.EventData;
import asynchomework.eventapi.EventName;
import asynchomework.eventapi.StreamEventType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AccountStreamEvent(
    StreamEventType eventType,
    String accountPublicId,
    String userPublicId,
    BigDecimal balance,
    OffsetDateTime creationTime
) implements EventData {
  @Override
  public EventName getEventName() {
    return EventName.ACCOUNT_STREAM;
  }
}

