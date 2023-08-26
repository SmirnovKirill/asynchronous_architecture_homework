package asynchomework.eventapi;

public enum Topic {
  USER("user"),
  USER_STREAM("user_stream"),
  TASK("task"),
  TASK_STREAM("task_stream");

  private final String name;

  Topic(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
