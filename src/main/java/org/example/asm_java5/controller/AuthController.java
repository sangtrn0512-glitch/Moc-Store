package org.example.asm_java5.controller;

import jakarta.servlet.http.*;
import org.example.asm_java5.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
  private final AuthService auth;

  public AuthController(AuthService auth) {
    this.auth = auth;
  }

  @GetMapping("/auth/login")
  public String login() {
    return "login";
  }

  @PostMapping("/auth/login")
  public String login(
      @RequestParam String username,
      @RequestParam String password,
      Model model,
      HttpServletRequest request) {
    try {
      auth.login(username, password);
      request.changeSessionId();
      String target = (String) request.getSession().getAttribute("returnUrl");
      request.getSession().removeAttribute("returnUrl");
      if (target != null && target.startsWith("/") && !target.startsWith("//"))
        return "redirect:" + target;
      return auth.current().isAdmin() ? "redirect:/admin/product/index" : "redirect:/";
    } catch (IllegalArgumentException e) {
      model.addAttribute("error", e.getMessage());
      return "login";
    }
  }

  @PostMapping("/auth/logout")
  public String logout() {
    auth.logout();
    return "redirect:/auth/login";
  }
}
