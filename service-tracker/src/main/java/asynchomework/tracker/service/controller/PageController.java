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
  public String index(@CookieValue(value = "userPublicId", required = false) String userPublicId, Model model) {
    if (StringUtils.isBlank(userPublicId)) {
      return "redirect:http://localhost:8080/sign_in.html";
    }

    model.addAttribute("canViewOwnTasks", authService.canViewOwnTasks(userPublicId));
    model.addAttribute("canViewAllTasks", authService.canViewAllTasks(userPublicId));
    model.addAttribute("canShuffleTasks", authService.canShuffleTasks(userPublicId));
    return "index";
  }

  @GetMapping("/create_task.html")
  public String createTask(@CookieValue(value = "userPublicId", required = false) String userPublicId) {
    if (StringUtils.isBlank(userPublicId)) {
      return "redirect:http://localhost:8080/sign_in.html";
    }

    return "create_task";
  }
}

