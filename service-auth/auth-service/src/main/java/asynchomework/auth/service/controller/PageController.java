package asynchomework.auth.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
  @GetMapping("/add_user.html")
  public String html() {
    return "add_user";
  }
}
