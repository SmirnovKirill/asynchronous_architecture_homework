package asynchomework.accounting.service.db;

import asynchomework.accounting.service.domain.AccountingUserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "accounting_user")
public class AccountingUserDb {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private long userId;

  @Column(name = "user_public_id", nullable = false, unique = true)
  private String userPublicId;

  @Column(name = "user_name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_role", nullable = false)
  private AccountingUserRole role;

  @Column(name = "creation_time", nullable = false)
  private OffsetDateTime creationTime;

  protected AccountingUserDb() {
  }

  public AccountingUserDb(String userPublicId, String name, AccountingUserRole role, OffsetDateTime creationTime) {
    this.userPublicId = userPublicId;
    this.name = name;
    this.role = role;
    this.creationTime = creationTime;
  }

  public long getUserId() {
    return userId;
  }

  public String getUserPublicId() {
    return userPublicId;
  }

  public void setUserPublicId(String userPublicId) {
    this.userPublicId = userPublicId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AccountingUserRole getRole() {
    return role;
  }

  public void setRole(AccountingUserRole role) {
    this.role = role;
  }

  public OffsetDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(OffsetDateTime creationTime) {
    this.creationTime = creationTime;
  }
}
