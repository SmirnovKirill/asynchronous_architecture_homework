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
@Table(name = "account")
public class AccountDb {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_id")
  private long accountId;

  @Column(name = "account_public_id", nullable = false, unique = true)
  private String accountPublicId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private AccountingUserDb user;

  @Column(name = "balance", nullable = false)
  private BigDecimal balance;

  @Column(name = "creation_time", nullable = false)
  private OffsetDateTime creationTime;

  protected AccountDb() {
  }

  public AccountDb(String accountPublicId, AccountingUserDb user, BigDecimal balance, OffsetDateTime creationTime) {
    this.accountPublicId = accountPublicId;
    this.user = user;
    this.balance = balance;
    this.creationTime = creationTime;
  }

  public long getAccountId() {
    return accountId;
  }

  public String getAccountPublicId() {
    return accountPublicId;
  }

  public void setAccountPublicId(String accountPublicId) {
    this.accountPublicId = accountPublicId;
  }

  public AccountingUserDb getUser() {
    return user;
  }

  public void setUser(AccountingUserDb user) {
    this.user = user;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public OffsetDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(OffsetDateTime creationTime) {
    this.creationTime = creationTime;
  }
}