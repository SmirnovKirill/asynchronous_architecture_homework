package asynchomework.accounting.service.kafka;

import asynchomework.accounting.service.domain.Account;
import asynchomework.eventapi.Event;
import asynchomework.eventapi.EventData;
import asynchomework.eventapi.SchemaValidator;
import asynchomework.eventapi.StreamEventType;
import asynchomework.eventapi.Topic;
import asynchomework.eventapi.account.AccountBalanceChangedEvent;
import asynchomework.eventapi.account.AccountOperationType;
import asynchomework.eventapi.account.AccountStreamEvent;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducer {
  private final KafkaTemplate<String, String> template;

  public KafkaProducer(KafkaTemplate<String, String> template) {
    this.template = template;
  }

  public void sendAccountCreatedOrModifiedStream(Account account, StreamEventType eventType) {
    AccountStreamEvent accountStreamEvent = new AccountStreamEvent(
        eventType,
        account.accountPublicId(),
        account.user().publicId(),
        account.balance(),
        account.creationTime()
    );

    validateAndSend(Topic.ACCOUNT_STREAM, account.accountPublicId(), accountStreamEvent);
  }

  public void sendAccountDeletedStream(String accountPublicId) {
    AccountStreamEvent accountStreamEvent = new AccountStreamEvent(
        StreamEventType.DELETE,
        accountPublicId,
        null,
        null,
        null
    );

    validateAndSend(Topic.ACCOUNT_STREAM, accountPublicId, accountStreamEvent);
  }

  public void sendAccountBalanceChangedStream(String accountPublicId, BigDecimal amount, AccountOperationType operationType) {
    AccountBalanceChangedEvent accountBalanceChangedEvent = new AccountBalanceChangedEvent(
        accountPublicId,
        amount,
        operationType
    );

    validateAndSend(Topic.ACCOUNT_BALANCE, accountPublicId, accountBalanceChangedEvent);
  }

  private <T extends EventData> void validateAndSend(Topic topic, String key, T data) {
    Event<T> event = new Event<>(UUID.randomUUID().toString(), 1, data.getEventName(), OffsetDateTime.now(), "accounting", data);
    SchemaValidator.validate(event);
    this.template.send(topic.getName(), key, event.toJsonString());
  }
}
