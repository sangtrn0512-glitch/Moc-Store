package org.example.asm_java5.controller.admin;

import org.example.asm_java5.entity.Account;
import org.example.asm_java5.repository.OrderRepository;
import org.example.asm_java5.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountAController {
  private final AccountService accounts;
  private final AuthService auth;
  private final OrderRepository orders;

  public AccountAController(AccountService accounts, AuthService auth, OrderRepository orders) {
    this.accounts = accounts;
    this.auth = auth;
    this.orders = orders;
  }

  @GetMapping("/admin/account/index")
  public String index(
      @RequestParam(defaultValue = "") String keyword,
      @RequestParam(defaultValue = "id") String sort,
      @RequestParam(defaultValue = "0") int page,
      Model model) {
    java.util.List<Account> filtered = new java.util.ArrayList<>();
    for (Account item : accounts.findAll()) {
      if ((item.getUsername() + " " + item.getFullName())
          .toLowerCase(java.util.Locale.ROOT)
          .contains(keyword.trim().toLowerCase(java.util.Locale.ROOT))) filtered.add(item);
    }
    if (sort.equals("name")) filtered.sort(java.util.Comparator.comparing(Account::getUsername));
    model.addAttribute("keyword", keyword);
    model.addAttribute("sort", sort);
    model.addAttribute(
        "accounts", org.example.asm_java5.service.ListPage.slice(filtered, page, model));
    model.addAttribute("account", new Account());
    return "admin/account";
  }

  @GetMapping("/admin/account/edit/{id}")
  public String edit(@PathVariable String id, Model model) {
    model.addAttribute(
        "account",
        accounts
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản")));
    model.addAttribute("accounts", accounts.findAll());
    model.addAttribute("editing", true);
    return "admin/account";
  }

  @PostMapping({"/admin/account/create", "/admin/account/update"})
  public String save(
      @RequestParam String username,
      @RequestParam String fullName,
      @RequestParam String email,
      @RequestParam(defaultValue = "") String password,
      @RequestParam(defaultValue = "false") boolean admin,
      @RequestParam(defaultValue = "false") boolean activated,
      jakarta.servlet.http.HttpServletRequest request,
      RedirectAttributes redirect) {
    try {
      boolean editing = request.getServletPath().endsWith("update");
      Account account;
      if (editing)
        account =
            accounts
                .findById(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản"));
      else {
        if (accounts.existsByUsername(username.trim()))
          throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        account = new Account();
        account.setUsername(username.trim());
      }
      if (username.equals(auth.current().getUsername()) && (!admin || !activated))
        throw new IllegalArgumentException("Không thể tự bỏ quyền hoặc khóa tài khoản đang dùng");
      account.setFullName(fullName.trim());
      account.setEmail(email.trim());
      accounts.validate(account);
      if (!editing) {
        accounts.checkPassword(password);
        account.setPassword(password);
        accounts.register(account);
      } else if (!password.isEmpty()) accounts.setPassword(account, password);
      account.setAdmin(admin);
      account.setActivated(activated);
      if (!activated) {
        account.setActivationToken(null);
        account.setActivationExpiresAt(null);
        account.setResetToken(null);
        account.setResetExpiresAt(null);
      }
      accounts.save(account);
      redirect.addFlashAttribute("success", "Đã lưu tài khoản");
    } catch (IllegalArgumentException | org.springframework.dao.DataIntegrityViolationException e) {
      redirect.addFlashAttribute("error", "Không thể lưu: thông tin không hợp lệ hoặc trùng");
    }
    return "redirect:/admin/account/index";
  }

  @PostMapping("/admin/account/delete/{id}")
  public String delete(@PathVariable String id, RedirectAttributes redirect) {
    try {
      if (id.equals(auth.current().getUsername()))
        throw new IllegalArgumentException("Không thể tự xóa tài khoản đang dùng");
      if (orders.existsByAccount_Username(id))
        throw new IllegalArgumentException("Tài khoản có đơn hàng, hãy khóa thay vì xóa");
      accounts.deleteById(id);
      redirect.addFlashAttribute("success", "Đã xóa tài khoản");
    } catch (IllegalArgumentException | org.springframework.dao.DataIntegrityViolationException e) {
      redirect.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/account/index";
  }
}
