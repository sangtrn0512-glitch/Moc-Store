package org.example.asm_java5.service;

import static org.example.asm_java5.support.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.example.asm_java5.entity.Account;
import org.example.asm_java5.repository.AccountRepository;
import org.example.asm_java5.support.CaseIds;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockHttpSession;

class AuthServiceTests {
  AccountRepository accounts;
  MockHttpSession session;
  AuthService auth;
  Account user;

  // Tạo Repository giả, session mới và tài khoản mẫu trước mỗi test đăng nhập.
  @BeforeEach
  void setup() {
    accounts = mock(AccountRepository.class);
    session = new MockHttpSession();
    auth = new AuthService(accounts, session);
    user = account("qa", false);
    when(accounts.findById("qa")).thenReturn(Optional.of(user));
  }

  // Kiểm tra đăng nhập lưu đúng tài khoản và dấu mật khẩu vào session.
  @Test
  @Tag("smoke")
  @CaseIds("TC-046")
  void loginStoresIdentityAndPasswordStamp() {
    auth.login(" qa ", "Test@1234");
    assertEquals("qa", session.getAttribute("username"));
    assertEquals(user.getPassword(), session.getAttribute("authStamp"));
    assertSame(user, auth.current());
  }

  // Kiểm tra sai mật khẩu hoặc chưa kích hoạt thì không đăng nhập được.
  @Test
  @CaseIds("TC-047")
  void wrongPasswordAndInactiveCannotLogin() {
    assertThrows(IllegalArgumentException.class, () -> auth.login("qa", "wrong"));
    user.setActivated(false);
    assertThrows(IllegalArgumentException.class, () -> auth.login("qa", "Test@1234"));
    assertNull(session.getAttribute("username"));
  }

  // Kiểm tra đổi mật khẩu làm phiên đăng nhập cũ mất hiệu lực.
  @Test
  @CaseIds("TC-054")
  void changedHashRevokesExistingSession() {
    auth.login("qa", "Test@1234");
    user.setPassword("changed-hash");
    assertNull(auth.current());
    assertNull(session.getAttribute("username"));
  }

  // Kiểm tra khóa tài khoản làm phiên đăng nhập cũ mất hiệu lực.
  @Test
  @CaseIds("TC-098")
  void inactiveAccountRevokesExistingSession() {
    auth.login("qa", "Test@1234");
    user.setActivated(false);
    assertNull(auth.current());
  }

  // Kiểm tra đăng xuất hủy session.
  @Test
  @CaseIds("TC-053")
  void logoutInvalidatesSession() {
    auth.login("qa", "Test@1234");
    auth.logout();
    assertTrue(session.isInvalid());
  }
}
