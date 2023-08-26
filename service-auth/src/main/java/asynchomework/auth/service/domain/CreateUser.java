package asynchomework.auth.service.domain;

public record CreateUser(
    String name,
    int beakSize,
    UserRole role
) {
}
