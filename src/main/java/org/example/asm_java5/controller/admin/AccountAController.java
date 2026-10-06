package org.example.asm_java5.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountAController {
  @GetMapping({"/admin/account/index"})
  public String page0() {
    return "admin/account";
  }
}
