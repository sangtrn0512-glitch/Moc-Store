package org.example.asm_java5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {
  @GetMapping({"/cart/index"})
  public String page0() {
    return "cart";
  }
}
