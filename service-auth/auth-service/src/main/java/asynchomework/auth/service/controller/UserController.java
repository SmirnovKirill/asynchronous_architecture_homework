package asynchomework.auth.service.controller;

import asynchomework.auth.api.UserDto;
import asynchomework.auth.api.UserRoleDto;
import asynchomework.auth.api.create.CreateUserRequestDto;
import asynchomework.auth.api.create.CreateUserResponseDto;
import asynchomework.auth.service.domain.CreateUser;
import asynchomework.auth.service.domain.User;
import asynchomework.auth.service.domain.UserRole;
import asynchomework.auth.service.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/user",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public CreateUserResponseDto addUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
    if (StringUtils.isBlank(createUserRequestDto.name())) {
      throw new IllegalArgumentException("Name can't be empty");
    }
    if (createUserRequestDto.beakSize() < 1) {
      throw new IllegalArgumentException("Beak size has to be positive");
    }

    CreateUser createUser = new CreateUser(
        createUserRequestDto.name(),
        createUserRequestDto.beakSize(),
        UserRole.valueOf(createUserRequestDto.role().toString())
    );
    User user = userService.save(createUser);
    return new CreateUserResponseDto(userDtoFrom(user));
  }

  @GetMapping
  public UserDto getUser(@RequestParam("user_id") int userId) {
    return userDtoFrom(userService.get(userId).orElseThrow());
  }

  private static UserDto userDtoFrom(User user) {
    return new UserDto(
        user.id(),
        user.name(),
        user.beakSize(),
        UserRoleDto.valueOf(user.role().toString()),
        user.creationTime()
    );
  }
}
