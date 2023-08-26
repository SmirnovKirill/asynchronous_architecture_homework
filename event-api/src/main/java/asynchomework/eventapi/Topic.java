package asynchomework.eventapi;

public enum Topic {
  USER_STREAM("user_stream"),
  USER("user"),
  TASK_STREAM("task_stream"),
  TASK("task"),
  ACCOUNT_STREAM("account_stream"),
  ACCOUNT_BALANCE("account_balance");

  private final String name;

  Topic(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
