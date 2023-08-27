package asynchomework.accounting.service.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "accounting_task")
public class AccountingTaskDb {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "task_id")
  private long taskId;

  @Column(name = "task_public_id", nullable = false, unique = true)
  private String taskPublicId;

  @Column(name = "title", nullable = false)
  private String title;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignee_user_id", nullable = false)
  private AccountingUserDb assignee;

  @Column(name = "assign_fee", nullable = false)
  private BigDecimal assignFee;

  @Column(name = "resolve_price", nullable = false)
  private BigDecimal resolvePrice;

  @Column(name = "creation_time", nullable = false)
  private OffsetDateTime creationTime;

  protected AccountingTaskDb() {
  }

  public AccountingTaskDb(
      String taskPublicId,
      String title,
      AccountingUserDb assignee,
      BigDecimal assignFee,
      BigDecimal resolvePrice,
      OffsetDateTime creationTime
  ) {
    this.taskPublicId = taskPublicId;
    this.title = title;
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

  public AccountingUserDb getAssignee() {
    return assignee;
  }

  public void setAssignee(AccountingUserDb assignee) {
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
