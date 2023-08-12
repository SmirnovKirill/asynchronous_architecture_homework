package asynchomework.auth.service.controller;

import asynchomework.auth.service.domain.CreateUser;
import asynchomework.auth.service.domain.User;
import asynchomework.auth.service.domain.UserRole;
import asynchomework.auth.service.service.UserService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestDataGeneratorController {
  private final UserService userService;
  public TestDataGeneratorController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/generate")
  public void generate() {
    List<User> allUsers = userService.getAll();
    allUsers.stream().map(User::id).forEach(userService::removeUser);

    for (int i = 1; i <= 10; i++) {
      userService.save(new CreateUser("admin-%d".formatted(i), 1000000 + i, UserRole.ADMINISTRATOR));
    }

    for (int i = 1; i <= 10; i++) {
      userService.save(new CreateUser("manager-%d".formatted(i), 100000 + i, UserRole.MANAGER));
    }

    for (int i = 1; i <= 10; i++) {
      userService.save(new CreateUser("accountant-%d".formatted(i), 10000 + i, UserRole.ACCOUNTANT));
    }

    for (int i = 1; i <= 100; i++) {
      userService.save(new CreateUser("employee-%d".formatted(i), i, UserRole.EMPLOYEE));
    }
  }
}
