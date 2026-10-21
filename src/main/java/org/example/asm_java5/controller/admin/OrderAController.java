package org.example.asm_java5.controller.admin;

import java.util.*;
import org.example.asm_java5.entity.*;
import org.example.asm_java5.entity.Order;
import org.example.asm_java5.model.CartItem;
import org.example.asm_java5.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderAController {
  private final OrderService orders;
  private final AccountService accounts;
  private final ProductService products;

  public OrderAController(OrderService orders, AccountService accounts, ProductService products) {
    this.orders = orders;
    this.accounts = accounts;
    this.products = products;
  }

  @GetMapping("/admin/order/index")
  public String index(
      @RequestParam(defaultValue = "") String keyword,
      @RequestParam(defaultValue = "id") String sort,
      @RequestParam(defaultValue = "0") int page,
      Model model) {
    java.util.List<Order> filtered = new java.util.ArrayList<>();
    for (Order item : orders.findAll()) {
      if ((item.getId() + " " + item.getAccount().getUsername() + " " + item.getStatus())
          .toLowerCase(java.util.Locale.ROOT)
          .contains(keyword.trim().toLowerCase(java.util.Locale.ROOT))) filtered.add(item);
    }
    if (sort.equals("name")) filtered.sort(java.util.Comparator.comparing(Order::getId));
    model.addAttribute("keyword", keyword);
    model.addAttribute("sort", sort);
    model.addAttribute(
        "orders", org.example.asm_java5.service.ListPage.slice(filtered, page, model));
    model.addAttribute("accounts", accounts.findAll());
    model.addAttribute("products", products.findAll());
    return "admin/order";
  }

  @GetMapping("/admin/order/edit/{id}")
  public String edit(@PathVariable Long id, Model model) {
    Order order =
        orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn"));
    model.addAttribute("products", products.findAll());
    model.addAttribute("order", order);
    model.addAttribute("details", orders.details(id));
    model.addAttribute("total", orders.total(id));
    return "admin/order-edit";
  }

  @PostMapping("/admin/order/create")
  public String create(
      @RequestParam String username,
      @RequestParam Integer productId,
      @RequestParam int quantity,
      @RequestParam String address,
      @RequestParam String phone,
      RedirectAttributes redirect) {
    try {
      Account account =
          accounts
              .findById(username)
              .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách"));
      Product product =
          products
              .findById(productId)
              .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm"));
      CartItem item = new CartItem();
      item.setProduct(product);
      item.setQuantity(quantity);
      orders.checkout(account, List.of(item), address, phone);
      redirect.addFlashAttribute("success", "Đã tạo đơn");
    } catch (IllegalArgumentException e) {
      redirect.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/order/index";
  }

  @PostMapping("/admin/order/update")
  public String update(
      @RequestParam Long id,
      @RequestParam String address,
      @RequestParam String phone,
      @RequestParam String status,
      RedirectAttributes redirect) {
    try {
      orders.update(id, address, phone, status);
      redirect.addFlashAttribute("success", "Đã cập nhật đơn");
    } catch (IllegalArgumentException e) {
      redirect.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/order/edit/" + id;
  }

  @PostMapping("/admin/order/delete/{id}")
  public String delete(@PathVariable Long id, RedirectAttributes redirect) {
    try {
      orders.deleteById(id);
      redirect.addFlashAttribute("success", "Đã xóa đơn");
    } catch (IllegalArgumentException e) {
      redirect.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/order/index";
  }

  @PostMapping("/admin/order/item")
  public String item(
      @RequestParam Long orderId,
      @RequestParam Integer productId,
      @RequestParam int quantity,
      RedirectAttributes redirect) {
    try {
      orders.saveItem(orderId, productId, quantity);
      redirect.addFlashAttribute("success", "Đã lưu dòng hàng");
    } catch (IllegalArgumentException e) {
      redirect.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/order/edit/" + orderId;
  }

  @PostMapping("/admin/order/item/delete")
  public String removeItem(
      @RequestParam Long orderId, @RequestParam Long detailId, RedirectAttributes redirect) {
    try {
      orders.removeItem(orderId, detailId);
      redirect.addFlashAttribute("success", "Đã xóa dòng hàng");
    } catch (IllegalArgumentException e) {
      redirect.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/order/edit/" + orderId;
  }
}
