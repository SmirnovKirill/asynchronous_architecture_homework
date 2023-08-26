package asynchomework.auth.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
  @GetMapping("/sign_up.html")
  public String signUp() {
    return "sign_up";
  }

  @GetMapping("/sign_in.html")
  public String signIn() {
    return "sign_in";
  }
}
