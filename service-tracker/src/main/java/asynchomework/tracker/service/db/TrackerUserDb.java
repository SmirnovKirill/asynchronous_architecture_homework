package asynchomework.tracker.service.db;

import asynchomework.tracker.service.domain.TrackerUserRole;
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
@Table(name = "tracker_user")
public class TrackerUserDb {
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
  private TrackerUserRole role;

  @Column(name = "creation_time", nullable = false)
  private OffsetDateTime creationTime;

  protected TrackerUserDb() {
  }

  public TrackerUserDb(String userPublicId, String name, TrackerUserRole role, OffsetDateTime creationTime) {
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

  public TrackerUserRole getRole() {
    return role;
  }

  public void setRole(TrackerUserRole role) {
    this.role = role;
  }

  public OffsetDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(OffsetDateTime creationTime) {
    this.creationTime = creationTime;
  }
}
