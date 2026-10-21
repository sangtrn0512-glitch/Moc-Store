package org.example.asm_java5.controller;

import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import org.example.asm_java5.entity.Order;
import org.example.asm_java5.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderController {
  private final OrderService orders;
  private final CartService cart;
  private final AuthService auth;

  public OrderController(OrderService orders, CartService cart, AuthService auth) {
    this.orders = orders;
    this.cart = cart;
    this.auth = auth;
  }

  @GetMapping("/order/checkout")
  public String checkout(Model model, HttpSession session) {
    model.addAttribute("items", cart.getItems());
    model.addAttribute("total", cart.getTotal());
    if (session.getAttribute("checkoutKey") == null)
      session.setAttribute("checkoutKey", UUID.randomUUID().toString());
    return "check-out";
  }

  @PostMapping("/order/checkout")
  public String checkout(
      @RequestParam String address,
      @RequestParam String phone,
      @RequestParam String checkoutKey,
      HttpSession session,
      RedirectAttributes redirect) {
    synchronized (session) {
      try {
        if (!checkoutKey.equals(session.getAttribute("checkoutKey")))
          throw new IllegalArgumentException("Yêu cầu đặt hàng đã được dùng. Hãy tải lại giỏ.");
        Order order = orders.checkout(auth.current(), cart.getItems(), address, phone);
        session.removeAttribute("checkoutKey");
        cart.clear();
        redirect.addFlashAttribute("success", "Đặt hàng thành công");
        return "redirect:/order/detail/" + order.getId();
      } catch (IllegalArgumentException e) {
        redirect.addFlashAttribute("error", e.getMessage());
        return "redirect:/order/checkout";
      }
    }
  }

  @GetMapping("/order/list")
  public String list(Model model) {
    model.addAttribute("orders", orders.mine(auth.current().getUsername()));
    return "order-list";
  }

  @GetMapping("/order/detail/{id}")
  public String detail(@PathVariable Long id, Model model) {
    Order order =
        orders.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (!order.getAccount().getUsername().equals(auth.current().getUsername()))
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    model.addAttribute("order", order);
    model.addAttribute("details", orders.details(id));
    model.addAttribute("total", orders.total(id));
    return "order-detail";
  }

  @GetMapping("/order/my-product-list")
  public String purchased(Model model) {
    model.addAttribute("details", orders.purchased(auth.current().getUsername()));
    return "my-product-list";
  }
}
