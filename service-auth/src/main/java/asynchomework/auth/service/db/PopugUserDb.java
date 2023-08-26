package asynchomework.auth.service.db;

import asynchomework.auth.service.domain.UserRole;
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
@Table(name = "popug_user")
public class PopugUserDb {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private long userId;

  @Column(name = "user_public_id", nullable = false, unique = true)
  private String userPublicId;

  @Column(name = "user_name", nullable = false)
  private String name;

  @Column(name = "beak_size", nullable = false)
  private int beakSize;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_role", nullable = false)
  private UserRole role;

  @Column(name = "creation_time", nullable = false)
  private OffsetDateTime creationTime;

  protected PopugUserDb() {
  }

  public PopugUserDb(String userPublicId, String name, int beakSize, UserRole role, OffsetDateTime creationTime) {
    this.userPublicId = userPublicId;
    this.name = name;
    this.beakSize = beakSize;
    this.role = role;
    this.creationTime = creationTime;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getUserPublicId() {
    return userPublicId;
  }

  public void setUserPublicId(String userPublicId) {
    this.userPublicId = userPublicId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getBeakSize() {
    return beakSize;
  }

  public void setBeakSize(int beakSize) {
    this.beakSize = beakSize;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public OffsetDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(OffsetDateTime creationTime) {
    this.creationTime = creationTime;
  }
}
