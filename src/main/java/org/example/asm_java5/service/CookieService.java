package org.example.asm_java5.service;

import jakarta.servlet.http.*;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
  private final HttpServletResponse response;

  public CookieService(HttpServletResponse response) {
    this.response = response;
  }

  public void set(String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    cookie.setAttribute("SameSite", "Lax");
    response.addCookie(cookie);
  }
}
