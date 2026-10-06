package org.example.asm_java5.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderAController {
  @GetMapping({"/admin/order/index"})
  public String page0() {
    return "admin/order";
  }
}
