package org.example.asm_java5.controller;

import org.example.asm_java5.entity.Account;
import org.example.asm_java5.service.AccountService;
import org.example.asm_java5.service.MailService;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {
  private final org.example.asm_java5.service.FileService fileService;
  private final AccountService accountService;
  private final MailService mailService;
  private final org.example.asm_java5.service.AuthService auth;

  @org.springframework.beans.factory.annotation.Value("${app.base-url:http://localhost:8080}")
  private String baseUrl;

  public AccountController(
      AccountService accountService,
      MailService mailService,
      org.example.asm_java5.service.AuthService auth,
      org.example.asm_java5.service.FileService fileService) {
    this.fileService = fileService;
    this.accountService = accountService;
    this.mailService = mailService;
    this.auth = auth;
  }

  @GetMapping("/account/sign-up")
  public String page0() {
    return "sign-up";
  }

  @PostMapping("/account/sign-up")
  public String register(
      @RequestParam("username") String username,
      @RequestParam("fullName") String fullName,
      @RequestParam("email") String email,
      @RequestParam("password") String password,
      Model model) {

    username = username.trim();
    fullName = fullName.trim();
    email = email.trim();

    String error = null;

    if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isBlank()) {
      error = "Vui lòng nhập đầy đủ thông tin";
    } else if (username.length() > 50 || fullName.length() > 100 || email.length() > 255) {
      error = "Thông tin nhập vượt quá độ dài cho phép";
    } else if (!email.contains("@") || !email.contains(".")) {
      error = "Email không hợp lệ";
    } else if (password.length() < 8
        || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
      error = "Mật khẩu cần ít nhất 8 ký tự và không quá 72 byte";
    } else if (accountService.existsByUsername(username)) {
      error = "Tên đăng nhập đã tồn tại";
    } else if (accountService.existsByEmail(email)) {
      error = "Email đã được sử dụng";
    }

    model.addAttribute("username", username);
    model.addAttribute("fullName", fullName);
    model.addAttribute("email", email);

    if (error != null) {
      model.addAttribute("error", error);
      return "sign-up";
    }

    Account account = new Account();
    account.setUsername(username);
    account.setFullName(fullName);
    account.setEmail(email);
    account.setPassword(password);

    Account savedAccount;
    try {
      savedAccount = accountService.register(account);
    } catch (IllegalArgumentException | org.springframework.dao.DataIntegrityViolationException e) {
      model.addAttribute("error", "Thông tin không hợp lệ hoặc tài khoản/email đã tồn tại");
      return "sign-up";
    }

    String activationLink =
        baseUrl + "/account/activate?token=" + savedAccount.getActivationToken();

    String body =
        "Chào bạn,\n\n"
            + "Bấm vào link dưới đây để kích hoạt tài khoản Mộc Store:\n"
            + activationLink
            + "\n\nLink có hiệu lực trong 24 giờ.";

    try {
      mailService.send(savedAccount.getEmail(), "Kích hoạt tài khoản Mộc Store", body);

      model.addAttribute(
          "success", "Đăng ký thành công. Hãy kiểm tra email để kích hoạt tài khoản.");
    } catch (MailException e) {
      model.addAttribute("error", "Tài khoản đã được tạo nhưng chưa gửi được email kích hoạt.");
    }

    return "sign-up";
  }

  @GetMapping("/account/activate")
  public String activate(
      @RequestParam("token") String token, RedirectAttributes redirectAttributes) {

    try {
      accountService.activate(token);
      redirectAttributes.addFlashAttribute(
          "success", "Kích hoạt thành công. Bạn có thể đăng nhập.");
    } catch (IllegalArgumentException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    }

    return "redirect:/auth/login";
  }

  @GetMapping("/account/edit-profile")
  public String profile(Model model) {
    model.addAttribute("account", auth.current());
    return "edit-profile";
  }

  @PostMapping("/account/edit-profile")
  public String profile(@RequestParam String fullName, @RequestParam String email, Model model) {
    Account account = auth.current();
    account.setFullName(fullName.trim());
    // Email changes require a new activation link.
    boolean changed = !account.getEmail().equalsIgnoreCase(email.trim());
    account.setEmail(email.trim());
    try {
      if (changed) {
        account.setActivationToken(java.util.UUID.randomUUID().toString());
        account.setActivationExpiresAt(java.time.LocalDateTime.now().plusHours(24));
        account.setActivated(false);
      }
      accountService.save(account);
      if (changed) {
        mailService.send(
            account.getEmail(),
            "Xác nhận email Mộc Store",
            baseUrl + "/account/activate?token=" + account.getActivationToken());
      }
      model.addAttribute(
          "success",
          changed ? "Đã lưu. Hãy kích hoạt email mới và đăng nhập lại." : "Đã lưu hồ sơ");
    } catch (IllegalArgumentException | org.springframework.dao.DataIntegrityViolationException e) {
      model.addAttribute("error", "Thông tin không hợp lệ hoặc email đã được sử dụng");
    } catch (MailException e) {
      model.addAttribute(
          "error", "Đã lưu nhưng gửi email thất bại. Hãy yêu cầu gửi lại kích hoạt.");
    }
    model.addAttribute("account", account);
    return "edit-profile";
  }

  @GetMapping("/account/change-password")
  public String change() {
    return "change-password";
  }

  @PostMapping("/account/change-password")
  public String change(
      @RequestParam String oldPassword,
      @RequestParam String password,
      @RequestParam String confirmPassword,
      Model model) {
    try {
      if (!password.equals(confirmPassword))
        throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
      accountService.changePassword(auth.current(), oldPassword, password);
      auth.logout();
      return "redirect:/auth/login";
    } catch (IllegalArgumentException e) {
      model.addAttribute("error", e.getMessage());
      return "change-password";
    }
  }

  @GetMapping("/account/forgot-password")
  public String forgot() {
    return "forgot-password";
  }

  @PostMapping("/account/forgot-password")
  public String forgot(@RequestParam String email, Model model) {
    Account account = accountService.prepareReset(email);
    try {
      if (account != null)
        mailService.send(
            account.getEmail(),
            "Đặt lại mật khẩu Mộc Store",
            "Link có hạn 30 phút: "
                + baseUrl
                + "/account/reset-password?token="
                + account.getResetToken());
      model.addAttribute(
          "success", "Nếu email thuộc tài khoản đang hoạt động, bạn sẽ nhận được hướng dẫn.");
    } catch (MailException e) {
      model.addAttribute("error", "Chưa gửi được thư. Vui lòng thử lại sau.");
    }
    return "forgot-password";
  }

  @GetMapping("/account/reset-password")
  public String reset(@RequestParam(defaultValue = "") String token, Model model) {
    model.addAttribute("token", token);
    return "reset-password";
  }

  @PostMapping("/account/reset-password")
  public String reset(
      @RequestParam String token,
      @RequestParam String password,
      @RequestParam String confirmPassword,
      Model model) {
    model.addAttribute("token", token);
    try {
      if (!password.equals(confirmPassword))
        throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
      accountService.reset(token, password);
      model.addAttribute("success", "Đã đổi mật khẩu. Bạn có thể đăng nhập.");
    } catch (IllegalArgumentException e) {
      model.addAttribute("error", e.getMessage());
    }
    return "reset-password";
  }

  @PostMapping("/account/resend-activation")
  public String resend(@RequestParam String email, Model model) {
    Account account = accountService.prepareActivation(email);
    try {
      if (account != null)
        mailService.send(
            account.getEmail(),
            "Kích hoạt Mộc Store",
            baseUrl + "/account/activate?token=" + account.getActivationToken());
      model.addAttribute("success", "Nếu có tài khoản chờ kích hoạt, thư đã được gửi.");
    } catch (MailException e) {
      model.addAttribute("error", "Gửi thư thất bại, hãy thử lại sau.");
    }
    return "forgot-password";
  }

  @PostMapping("/account/photo")
  public String photo(
      @RequestParam org.springframework.web.multipart.MultipartFile photo,
      RedirectAttributes redirect) {
    try {
      Account account = auth.current();
      account.setPhoto(fileService.save(photo));
      accountService.save(account);
      redirect.addFlashAttribute("success", "Đã cập nhật ảnh");
    } catch (IllegalArgumentException e) {
      redirect.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/account/edit-profile";
  }
}
