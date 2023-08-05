package asynchomework.auth.api.create;

import asynchomework.auth.api.UserRoleDto;

public record CreateUserRequestDto(
    String name,
    int beakSize,
    UserRoleDto role
) {
}
