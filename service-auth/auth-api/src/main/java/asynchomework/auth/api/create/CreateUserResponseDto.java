package asynchomework.auth.api.create;

import asynchomework.auth.api.UserDto;

public record CreateUserResponseDto(
    UserDto user
) {
}
