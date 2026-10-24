package org.example.asm_java5.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import org.example.asm_java5.entity.Account;
import org.example.asm_java5.support.*;
import org.junit.jupiter.api.*;
import org.springframework.mail.MailSendException;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AccountIntegrationTests extends DatabaseTestSupport {
  MockMvc mvc;

  // Tạo MockMvc từ cấu hình Spring để gửi request trước mỗi test.
  @BeforeEach
  void mvc() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  MockHttpSession session(Account a) {
    MockHttpSession s = new MockHttpSession();
    s.setAttribute("csrf", "token");
    if (a != null) {
      s.setAttribute("username", a.getUsername());
      s.setAttribute("authStamp", a.getPassword());
    }
    return s;
  }

  // Kiểm tra lỗi gửi email vẫn giữ tài khoản chưa kích hoạt và báo lỗi.
  @Test
  @CaseIds("TC-033")
  void smtpFailureKeepsInactiveAccountAndShowsError() throws Exception {
    doThrow(new MailSendException("simulated SMTP failure"))
        .when(mail)
        .send(anyString(), anyString(), anyString());
    mvc.perform(
            post("/account/sign-up")
                .session(session(null))
                .param("csrf", "token")
                .param("username", "qa_new")
                .param("fullName", "Nguyễn QA")
                .param("email", "qa_new@example.invalid")
                .param("password", "Test@1234"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("error"));
    assertFalse(accounts.findById("qa_new").orElseThrow().isActivated());
  }

  // Kiểm tra sửa hồ sơ không làm đổi quyền và trạng thái kích hoạt.
  @Test
  @CaseIds("TC-034")
  void updateProfilePreservesRoleAndActivation() throws Exception {
    mvc.perform(
            post("/account/edit-profile")
                .session(session(user))
                .param("csrf", "token")
                .param("fullName", "Nguyễn An")
                .param("email", user.getEmail()))
        .andExpect(model().attributeExists("success"));
    Account saved = accounts.findById(user.getUsername()).orElseThrow();
    assertEquals("Nguyễn An", saved.getFullName());
    assertTrue(saved.isActivated());
    assertFalse(saved.isAdmin());
    verifyNoInteractions(mail);
  }

  // Kiểm tra đổi email yêu cầu kích hoạt lại và hủy phiên đăng nhập.
  @Test
  @CaseIds("TC-035")
  void newEmailRequiresReactivationAndRevokesSession() throws Exception {
    MockHttpSession s = session(user);
    mvc.perform(
            post("/account/edit-profile")
                .session(s)
                .param("csrf", "token")
                .param("fullName", user.getFullName())
                .param("email", "changed@example.invalid"))
        .andExpect(model().attributeExists("success"));
    Account saved = accounts.findById(user.getUsername()).orElseThrow();
    assertEquals("changed@example.invalid", saved.getEmail());
    assertFalse(saved.isActivated());
    assertNotNull(saved.getActivationToken());
    verify(mail).send(eq(saved.getEmail()), anyString(), contains(saved.getActivationToken()));
    mvc.perform(get("/order/list").session(s)).andExpect(redirectedUrl("/auth/login"));
    accountService.activate(saved.getActivationToken());
    assertTrue(accounts.findById(user.getUsername()).orElseThrow().isActivated());
  }

  // Kiểm tra hồ sơ không hợp lệ không được lưu.
  @Test
  @CaseIds("TC-036")
  void invalidProfileDoesNotWrite() throws Exception {
    mvc.perform(
            post("/account/edit-profile")
                .session(session(user))
                .param("csrf", "token")
                .param("fullName", " ")
                .param("email", user.getEmail()))
        .andExpect(model().attributeExists("error"));
    assertEquals(
        user.getFullName(), accounts.findById(user.getUsername()).orElseThrow().getFullName());
  }

  // Kiểm tra xác nhận mật khẩu sai thì không đổi mật khẩu.
  @Test
  @CaseIds("TC-038")
  void passwordConfirmationMismatchDoesNotChangeHash() throws Exception {
    String hash = user.getPassword();
    mvc.perform(
            post("/account/change-password")
                .session(session(user))
                .param("csrf", "token")
                .param("oldPassword", "Test@1234")
                .param("password", "New@12345")
                .param("confirmPassword", "different"))
        .andExpect(model().attributeExists("error"));
    assertEquals(hash, accounts.findById(user.getUsername()).orElseThrow().getPassword());
  }

  // Kiểm tra xác nhận đặt lại sai thì giữ nguyên token và mật khẩu.
  @Test
  @CaseIds("TC-043")
  void resetConfirmationMismatchPreservesTokenAndPassword() throws Exception {
    Account a = accountService.prepareReset(user.getEmail());
    String hash = a.getPassword(), token = a.getResetToken();
    mvc.perform(
            post("/account/reset-password")
                .session(session(null))
                .param("csrf", "token")
                .param("token", token)
                .param("password", "Reset@123")
                .param("confirmPassword", "different"))
        .andExpect(model().attributeExists("error"));
    var saved = accounts.findById(a.getUsername()).orElseThrow();
    assertEquals(hash, saved.getPassword());
    assertEquals(token, saved.getResetToken());
  }

  // Kiểm tra token kích hoạt và đặt lại mật khẩu không dùng lại được.
  @Test
  @CaseIds({"TC-031", "TC-042"})
  void activationAndResetTokensCannotBeReused() {
    user.setActivated(false);
    user.setActivationToken("activate-once");
    user.setActivationExpiresAt(LocalDateTime.now().plusHours(1));
    accounts.save(user);
    accountService.activate("activate-once");
    assertThrows(IllegalArgumentException.class, () -> accountService.activate("activate-once"));
    String reset = accountService.prepareReset(user.getEmail()).getResetToken();
    accountService.reset(reset, "Reset@123");
    assertThrows(IllegalArgumentException.class, () -> accountService.reset(reset, "Again@123"));
    assertTrue(
        new BCryptPasswordEncoder()
            .matches(
                "Reset@123", accounts.findById(user.getUsername()).orElseThrow().getPassword()));
  }

  // Kiểm tra giới hạn độ dài email có cú pháp hợp lệ.
  @Test
  @CaseIds("TC-028")
  void emailLengthBoundaryWithValidSyntax() {
    Account a = accounts.findById(user.getUsername()).orElseThrow();
    a.setEmail("a".repeat(249) + "@b.com");
    assertEquals(255, a.getEmail().length());
    assertDoesNotThrow(() -> accountService.save(a));
    a.setEmail("a".repeat(250) + "@b.com");
    assertThrows(IllegalArgumentException.class, () -> accountService.save(a));
    assertEquals(255, accounts.findById(user.getUsername()).orElseThrow().getEmail().length());
  }
}
