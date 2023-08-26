package asynchomework.accounting.service.kafka;

import asynchomework.accounting.service.service.AccountService;
import asynchomework.accounting.service.service.AccountingService;
import asynchomework.accounting.service.service.AccountingTaskService;
import asynchomework.accounting.service.service.AccountingUserService;
import asynchomework.eventapi.Event;
import asynchomework.eventapi.EventName;
import asynchomework.eventapi.SchemaValidator;
import asynchomework.eventapi.task.TaskCreatedEvent;
import asynchomework.eventapi.task.TaskResolvedEvent;
import asynchomework.eventapi.task.TaskStreamEvent;
import asynchomework.eventapi.user.UserCreatedEvent;
import asynchomework.eventapi.user.UserStreamEvent;

public class KafkaListener {
  private final AccountingUserService accountingUserService;
  private final AccountingTaskService accountingTaskService;
  private final AccountService accountService;
  private final AccountingService accountingService;

  public KafkaListener(
      AccountingUserService accountingUserService,
      AccountingTaskService accountingTaskService,
      AccountService accountService,
      AccountingService accountingService
  ) {
    this.accountingUserService = accountingUserService;
    this.accountingTaskService = accountingTaskService;
    this.accountService = accountService;
    this.accountingService = accountingService;
  }

  @org.springframework.kafka.annotation.KafkaListener(topics = "user_stream")
  public void processUserStreamEvent(String eventString) {
    SchemaValidator.validate(eventString, EventName.USER_STREAM, 1);
    Event<UserStreamEvent> userStreamEvent = Event.eventFromString(eventString);
    accountingUserService.processUserStreamEvent(userStreamEvent.data());
  }

  @org.springframework.kafka.annotation.KafkaListener(topics = "task_stream")
  public void processTaskStreamEvent(String eventString) {
    Event<TaskStreamEvent> taskStreamEvent = Event.eventFromString(eventString);
    if (taskStreamEvent.eventVersion() == 1) {
      SchemaValidator.validate(eventString, EventName.TASK_STREAM, 1);
    } else if (taskStreamEvent.eventVersion() == 2) {
      SchemaValidator.validate(eventString, EventName.TASK_STREAM, 2);
    } else {
      throw new IllegalStateException("Unexpected version %d".formatted(taskStreamEvent.eventVersion()));
    }

    accountingTaskService.processTaskStreamEvent(taskStreamEvent.data());
  }

  @org.springframework.kafka.annotation.KafkaListener(topics = "user")
  public void processUserCreatedEvent(String eventString) {
    Event<?> event = Event.eventFromString(eventString);
    if (event.eventName() == EventName.USER_CREATED) {
      @SuppressWarnings("unchecked")
      Event<UserCreatedEvent> userCreatedEventEvent = (Event<UserCreatedEvent>) event;
      SchemaValidator.validate(eventString, EventName.USER_CREATED, 1);
      accountingService.createAccount(userCreatedEventEvent.data().userPublicId());
    }
  }

  @org.springframework.kafka.annotation.KafkaListener(topics = "task")
  public void processTaskEvent(String eventString) {
    Event<?> event = Event.eventFromString(eventString);
    switch (event.eventName()) {
      case TASK_CREATED -> {
        @SuppressWarnings("unchecked")
        Event<TaskCreatedEvent> taskCreatedEvent = (Event<TaskCreatedEvent>) event;
        SchemaValidator.validate(eventString, EventName.TASK_CREATED, 1);
        accountingService.withdrawMoneyForTask(taskCreatedEvent.data().taskPublicId(), taskCreatedEvent.data().creationTime());
      }
      case TASK_RESOLVED -> {
        @SuppressWarnings("unchecked")
        Event<TaskResolvedEvent> taskResolvedEvent = (Event<TaskResolvedEvent>) event;
        SchemaValidator.validate(eventString, EventName.TASK_RESOLVED, 1);
        accountingService.depositMoneyForTask(taskResolvedEvent.data().taskPublicId(), taskResolvedEvent.data().resolveTime());
      }
    }
  }
}
