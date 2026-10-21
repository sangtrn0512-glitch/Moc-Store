package org.example.asm_java5.controller;

import org.example.asm_java5.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {
  private final CartService cartService;

  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @GetMapping({"/cart/index"})
  public String page0(Model model) {
    model.addAttribute("items", cartService.getItems());
    model.addAttribute("total", cartService.getTotal());
    return "cart";
  }

  @PostMapping("/cart/add/{id}")
  public String add(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

    try {
      cartService.add(id);
      redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ");
    } catch (IllegalArgumentException | IllegalStateException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    }

    return "redirect:/product/detail/" + id;
  }

  @PostMapping("/cart/update")
  public String update(
      @RequestParam("productId") Integer productId,
      @RequestParam("quantity") int quantity,
      RedirectAttributes redirectAttributes) {

    try {
      cartService.update(productId, quantity);
    } catch (IllegalArgumentException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    }

    return "redirect:/cart/index";
  }

  @PostMapping("/cart/remove/{id}")
  public String remove(@PathVariable("id") Integer id) {
    cartService.remove(id);
    return "redirect:/cart/index";
  }

  @PostMapping("/cart/clear")
  public String clear() {
    cartService.clear();
    return "redirect:/cart/index";
  }
}
