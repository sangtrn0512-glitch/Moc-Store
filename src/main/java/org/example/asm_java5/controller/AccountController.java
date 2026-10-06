package org.example.asm_java5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
  @GetMapping({"/account/sign-up"})
  public String page0() {
    return "sign-up";
  }

  @GetMapping({"/account/edit-profile"})
  public String page1() {
    return "edit-profile";
  }

  @GetMapping({"/account/forgot-password"})
  public String page2() {
    return "forgot-password";
  }

  @GetMapping({"/account/change-password"})
  public String page3() {
    return "change-password";
  }
}
