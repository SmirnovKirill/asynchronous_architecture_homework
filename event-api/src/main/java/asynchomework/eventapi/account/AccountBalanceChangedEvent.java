package asynchomework.eventapi.account;

import asynchomework.eventapi.EventData;
import asynchomework.eventapi.EventName;
import java.math.BigDecimal;

public record AccountBalanceChangedEvent(
    String accountPublicId,
    BigDecimal amount,
    AccountOperationType operationType
) implements EventData {
  @Override
  public EventName getEventName() {
    return EventName.ACCOUNT_BALANCE_CHANGED;
  }
}
