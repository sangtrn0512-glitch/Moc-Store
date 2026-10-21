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

  @BeforeEach
  void setup() {
    accounts = mock(AccountRepository.class);
    session = new MockHttpSession();
    auth = new AuthService(accounts, session);
    user = account("qa", false);
    when(accounts.findById("qa")).thenReturn(Optional.of(user));
  }

  @Test
  @Tag("smoke")
  @CaseIds("TC-046")
  void loginStoresIdentityAndPasswordStamp() {
    auth.login(" qa ", "Test@1234");
    assertEquals("qa", session.getAttribute("username"));
    assertEquals(user.getPassword(), session.getAttribute("authStamp"));
    assertSame(user, auth.current());
  }

  @Test
  @CaseIds("TC-047")
  void wrongPasswordAndInactiveCannotLogin() {
    assertThrows(IllegalArgumentException.class, () -> auth.login("qa", "wrong"));
    user.setActivated(false);
    assertThrows(IllegalArgumentException.class, () -> auth.login("qa", "Test@1234"));
    assertNull(session.getAttribute("username"));
  }

  @Test
  @CaseIds("TC-054")
  void changedHashRevokesExistingSession() {
    auth.login("qa", "Test@1234");
    user.setPassword("changed-hash");
    assertNull(auth.current());
    assertNull(session.getAttribute("username"));
  }

  @Test
  @CaseIds("TC-098")
  void inactiveAccountRevokesExistingSession() {
    auth.login("qa", "Test@1234");
    user.setActivated(false);
    assertNull(auth.current());
  }

  @Test
  @CaseIds("TC-053")
  void logoutInvalidatesSession() {
    auth.login("qa", "Test@1234");
    auth.logout();
    assertTrue(session.isInvalid());
  }
}
