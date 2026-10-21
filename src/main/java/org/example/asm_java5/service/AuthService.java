package org.example.asm_java5.service;

import jakarta.servlet.http.HttpSession;
import org.example.asm_java5.entity.Account;
import org.example.asm_java5.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final AccountRepository accounts;
  private final HttpSession session;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public AuthService(AccountRepository accounts, HttpSession session) {
    this.accounts = accounts;
    this.session = session;
  }

  public boolean login(String username, String password) {
    Account account = accounts.findById(username.trim()).orElse(null);
    if (account == null || !encoder.matches(password, account.getPassword())) {
      throw new IllegalArgumentException("Sai tên đăng nhập hoặc mật khẩu");
    }
    if (!account.isActivated())
      throw new IllegalArgumentException("Tài khoản chưa kích hoạt hoặc đã bị khóa");
    session.setAttribute("username", account.getUsername());
    session.setAttribute("authStamp", account.getPassword());
    return true;
  }

  public Account current() {
    String username = (String) session.getAttribute("username");
    if (username == null) return null;
    Account account = accounts.findById(username).orElse(null);
    if (account == null
        || !account.isActivated()
        || !account.getPassword().equals(session.getAttribute("authStamp"))) {
      session.removeAttribute("username");
      return null;
    }
    return account;
  }

  public void logout() {
    session.invalidate();
  }
}
