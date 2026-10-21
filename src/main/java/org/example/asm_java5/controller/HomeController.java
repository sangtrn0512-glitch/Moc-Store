package org.example.asm_java5.controller;

import org.example.asm_java5.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  private final ProductService productService;

  public HomeController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping({"/", "/home/index"})
  public String page0(Model model) {

    model.addAttribute("newProducts", productService.findNewProducts());

    model.addAttribute("discountProducts", productService.findDiscountProducts());

    model.addAttribute("bestSellingProducts", productService.findBestSellingProducts());

    return "home";
  }
}
