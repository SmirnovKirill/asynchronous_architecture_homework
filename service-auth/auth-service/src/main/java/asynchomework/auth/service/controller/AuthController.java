package asynchomework.auth.service.controller;

import asynchomework.auth.service.domain.AuthRequest;
import asynchomework.auth.service.domain.User;
import asynchomework.auth.service.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/auth",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public User auth(@RequestBody AuthRequest authRequest, HttpServletResponse servletResponse) {
    User user = userService.getByName(authRequest.name()).orElse(null);
    if (user == null) {
      throw new IllegalArgumentException("Incorrect login");
    }

    if (user.beakSize() != authRequest.beakSize()) {
      throw new IllegalArgumentException("Incorrect beak size");
    }

    servletResponse.addCookie(new Cookie("userId", String.valueOf(user.id())));
    return user;
  }
}
