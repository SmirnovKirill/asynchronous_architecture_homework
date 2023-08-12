package asynchomework.tracker.service.controller;

import asynchomework.tracker.service.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
  private final AuthService authService;

  public PageController(AuthService authService) {
    this.authService = authService;
  }

  @GetMapping
  public String index(@CookieValue(value = "userId", required = false) String userId, Model model) {
    if (StringUtils.isBlank(userId)) {
      return "redirect:http://localhost:8080/sign_in.html";
    }

    model.addAttribute("canViewOwnTasks", authService.canViewOwnTasks(userId));
    model.addAttribute("canViewAllTasks", authService.canViewAllTasks(userId));
    model.addAttribute("canShuffleTasks", authService.canShuffleTasks(userId));
    return "index";
  }

  @GetMapping("/create_task.html")
  public String createTask(@CookieValue(value = "userId", required = false) String userId) {
    if (StringUtils.isBlank(userId)) {
      return "redirect:http://localhost:8080/sign_in.html";
    }

    return "create_task";
  }
}

