package asynchomework.accounting.service.db;

import asynchomework.accounting.service.domain.LogExtraIds;
import asynchomework.accounting.service.domain.OperationType;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "account_log")
public class AccountLogDb {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_log_id")
  private long accountLogId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private AccountDb account;

  @Enumerated(EnumType.STRING)
  @Column(name = "operation_type", nullable = false)
  private OperationType operationType;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "operation_time", nullable = false)
  private OffsetDateTime operationTime;

  @Column(name = "extra_ids", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  private LogExtraIds extraIds;

  protected AccountLogDb() {
  }

  public AccountLogDb(AccountDb account, OperationType operationType, BigDecimal amount, OffsetDateTime operationTime, LogExtraIds extraIds) {
    this.account = account;
    this.operationType = operationType;
    this.amount = amount;
    this.operationTime = operationTime;
    this.extraIds = extraIds;
  }

  public long getAccountLogId() {
    return accountLogId;
  }

  public AccountDb getAccount() {
    return account;
  }

  public void setAccount(AccountDb account) {
    this.account = account;
  }

  public OperationType getOperationType() {
    return operationType;
  }

  public void setOperationType(OperationType operationType) {
    this.operationType = operationType;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public OffsetDateTime getOperationTime() {
    return operationTime;
  }

  public void setOperationTime(OffsetDateTime operationTime) {
    this.operationTime = operationTime;
  }

  public LogExtraIds getExtraIds() {
    return extraIds;
  }

  public void setExtraIds(LogExtraIds extraIds) {
    this.extraIds = extraIds;
  }
}