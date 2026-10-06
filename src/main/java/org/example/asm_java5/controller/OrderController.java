package org.example.asm_java5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
  @GetMapping({"/order/checkout"})
  public String page0() {
    return "check-out";
  }

  @GetMapping({"/order/list"})
  public String page1() {
    return "order-list";
  }

  @GetMapping({"/order/detail/{id}"})
  public String page2() {
    return "order-detail";
  }

  @GetMapping({"/order/my-product-list"})
  public String page3() {
    return "my-product-list";
  }
}
