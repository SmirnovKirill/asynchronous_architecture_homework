package asynchomework.auth.service.domain;

public record AuthRequest(
    String name,
    int beakSize
) {
}
