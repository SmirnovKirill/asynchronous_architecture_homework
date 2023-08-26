package asynchomework.tracker.service.db;

import asynchomework.tracker.service.domain.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "task")
public class TaskDb {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "task_id")
  private long taskId;

  @Column(name = "task_public_id", nullable = false, unique = true)
  private String taskPublicId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private TaskStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignee_user_id", nullable = false)
  private TrackerUserDb assignee;

  @Column(name = "assign_fee", nullable = false)
  private BigDecimal assignFee;

  @Column(name = "resolve_price", nullable = false)
  private BigDecimal resolvePrice;

  @Column(name = "creation_time", nullable = false)
  private OffsetDateTime creationTime;

  protected TaskDb() {
  }

  public TaskDb(
      String taskPublicId,
      String title,
      String description,
      TaskStatus status,
      TrackerUserDb assignee,
      BigDecimal assignFee,
      BigDecimal resolvePrice,
      OffsetDateTime creationTime
  ) {
    this.taskPublicId = taskPublicId;
    this.title = title;
    this.description = description;
    this.status = status;
    this.assignee = assignee;
    this.assignFee = assignFee;
    this.resolvePrice = resolvePrice;
    this.creationTime = creationTime;
  }

  public long getTaskId() {
    return taskId;
  }

  public void setTaskId(long taskId) {
    this.taskId = taskId;
  }

  public String getTaskPublicId() {
    return taskPublicId;
  }

  public void setTaskPublicId(String taskPublicId) {
    this.taskPublicId = taskPublicId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public TrackerUserDb getAssignee() {
    return assignee;
  }

  public void setAssignee(TrackerUserDb assignee) {
    this.assignee = assignee;
  }

  public BigDecimal getAssignFee() {
    return assignFee;
  }

  public void setAssignFee(BigDecimal assignFee) {
    this.assignFee = assignFee;
  }

  public BigDecimal getResolvePrice() {
    return resolvePrice;
  }

  public void setResolvePrice(BigDecimal resolvePrice) {
    this.resolvePrice = resolvePrice;
  }

  public OffsetDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(OffsetDateTime creationTime) {
    this.creationTime = creationTime;
  }
}
