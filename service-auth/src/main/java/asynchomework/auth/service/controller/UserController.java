package asynchomework.auth.service.controller;

import asynchomework.auth.service.domain.CreateUser;
import asynchomework.auth.service.domain.User;
import asynchomework.auth.service.service.UserService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  public User addUser(@RequestBody CreateUser createUserRequest) {
    if (StringUtils.isBlank(createUserRequest.name())) {
      throw new IllegalArgumentException("Name can't be empty");
    }
    if (createUserRequest.beakSize() < 1) {
      throw new IllegalArgumentException("Beak size has to be positive");
    }

    return userService.save(createUserRequest);
  }

  @GetMapping("all")
  public List<User> getAllUsers() {
    return userService.getAll();
  }

  @DeleteMapping
  public void removeUser(@RequestParam(name = "user_id") long userId) {
    userService.removeUser(userId);
  }
}
