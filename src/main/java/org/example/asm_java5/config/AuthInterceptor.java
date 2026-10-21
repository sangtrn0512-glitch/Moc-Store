package org.example.asm_java5.config;

import jakarta.servlet.http.*;
import java.util.UUID;
import org.example.asm_java5.entity.Account;
import org.example.asm_java5.service.AuthService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
  private final AuthService auth;

  public AuthInterceptor(AuthService auth) {
    this.auth = auth;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    HttpSession session = request.getSession();
    if (session.getAttribute("csrf") == null)
      session.setAttribute("csrf", UUID.randomUUID().toString());
    if ("POST".equals(request.getMethod())
        && !session.getAttribute("csrf").equals(request.getParameter("csrf"))) {
      response.sendError(403, "Phiên biểu mẫu không hợp lệ. Hãy tải lại trang.");
      return false;
    }
    String path = request.getRequestURI();
    boolean admin = path.startsWith("/admin/");
    boolean protectedPage =
        admin
            || path.startsWith("/order/")
            || path.equals("/account/edit-profile")
            || path.equals("/account/change-password")
            || path.equals("/account/photo");
    if (!protectedPage) return true;
    Account account = auth.current();
    if (account == null) {
      if ("GET".equals(request.getMethod())) session.setAttribute("returnUrl", path);
      response.sendRedirect("/auth/login");
      return false;
    }
    if (admin && !account.isAdmin()) {
      response.sendError(403, "Bạn không có quyền quản trị");
      return false;
    }
    return true;
  }
}
