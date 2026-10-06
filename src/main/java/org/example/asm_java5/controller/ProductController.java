package org.example.asm_java5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {
  @GetMapping({"/product/list", "/product/list-by-category/{id}"})
  public String page0() {
    return "product-list";
  }

  @GetMapping({"/product/detail/{id}"})
  public String page1() {
    return "product-detail";
  }
}
