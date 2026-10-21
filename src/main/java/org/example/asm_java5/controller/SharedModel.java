package org.example.asm_java5.controller;

import org.example.asm_java5.entity.Account;
import org.example.asm_java5.service.AuthService;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class SharedModel {
  private final AuthService auth;

  public SharedModel(AuthService auth) {
    this.auth = auth;
  }

  @ModelAttribute("currentAccount")
  public Account account() {
    return auth.current();
  }
}
