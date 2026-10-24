package org.example.asm_java5.service;

import static org.example.asm_java5.support.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import org.example.asm_java5.entity.Account;
import org.example.asm_java5.repository.AccountRepository;
import org.example.asm_java5.support.CaseIds;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class AccountServiceTests {
  AccountRepository repository;
  AccountService service;

  // Tạo Repository giả và AccountService mới trước mỗi test.
  @BeforeEach
  void setup() {
    repository = mock(AccountRepository.class);
    service = new AccountService(repository);
    lenient().when(repository.save(any())).thenAnswer(i -> i.getArgument(0));
  }

  // Kiểm tra đăng ký mã hóa mật khẩu và yêu cầu kích hoạt.
  @Test
  @Tag("smoke")
  @CaseIds("TC-024")
  void registerHashesAndRequiresActivation() {
    Account a = account("qa", true);
    a.setPassword("Test@1234");
    LocalDateTime before = LocalDateTime.now();
    service.register(a);
    assertFalse(a.isAdmin());
    assertFalse(a.isActivated());
    assertNotNull(a.getActivationToken());
    assertTrue(new BCryptPasswordEncoder().matches("Test@1234", a.getPassword()));
    assertTrue(a.getActivationExpiresAt().isAfter(before.plusHours(23)));
    assertTrue(
        a.getActivationExpiresAt().isBefore(LocalDateTime.now().plusHours(24).plusSeconds(1)));
    verify(repository).save(a);
  }

  // Kiểm tra email sai định dạng bị từ chối.
  @ParameterizedTest
  @ValueSource(strings = {"abc", "a@b", "a b@c.com"})
  @CaseIds("TC-027")
  void rejectInvalidEmail(String email) {
    Account a = account("qa", false);
    a.setEmail(email);
    assertThrows(IllegalArgumentException.class, () -> service.save(a));
    verify(repository, never()).save(any());
  }

  // Kiểm tra email đã tồn tại bị từ chối.
  @Test
  @CaseIds("TC-026")
  void rejectDuplicateEmail() {
    Account a = account("qa", false);
    when(repository.existsByEmailIgnoreCaseAndUsernameNot(a.getEmail(), "qa")).thenReturn(true);
    assertThrows(IllegalArgumentException.class, () -> service.save(a));
    verify(repository, never()).save(any());
  }

  // Kiểm tra giới hạn độ dài tên đăng nhập và họ tên.
  @ParameterizedTest
  @CsvSource({"50,100,true", "51,100,false", "50,101,false"})
  @CaseIds("TC-028")
  void profileLengthBoundaries(int usernameLength, int nameLength, boolean valid) {
    Account a = account("qa", false);
    a.setUsername("a".repeat(usernameLength));
    a.setFullName("a".repeat(nameLength));
    if (valid) assertDoesNotThrow(() -> service.save(a));
    else {
      assertThrows(IllegalArgumentException.class, () -> service.save(a));
      verify(repository, never()).save(any());
    }
  }

  // Kiểm tra mật khẩu quá ngắn hoặc chỉ có khoảng trắng bị từ chối.
  @ParameterizedTest
  @ValueSource(strings = {"1234567", "        "})
  @CaseIds("TC-029")
  void rejectWeakPassword(String password) {
    assertThrows(IllegalArgumentException.class, () -> service.checkPassword(password));
  }

  // Kiểm tra giới hạn số byte của mật khẩu khi mã hóa.
  @Test
  @CaseIds("TC-029")
  void passwordByteBoundaries() {
    assertDoesNotThrow(() -> service.checkPassword("a".repeat(8)));
    assertDoesNotThrow(() -> service.checkPassword("a".repeat(72)));
    assertThrows(IllegalArgumentException.class, () -> service.checkPassword("a".repeat(73)));
    assertDoesNotThrow(() -> service.checkPassword("á".repeat(25)));
    assertThrows(IllegalArgumentException.class, () -> service.checkPassword("á".repeat(37)));
  }

  // Kiểm tra token hợp lệ kích hoạt tài khoản và bị xóa sau khi dùng.
  @Test
  @CaseIds("TC-030")
  void activateValidTokenAndConsumeIt() {
    Account a = account("qa", false);
    a.setActivated(false);
    a.setActivationToken("valid");
    a.setActivationExpiresAt(LocalDateTime.now().plusHours(1));
    when(repository.findByActivationToken("valid")).thenReturn(Optional.of(a));
    service.activate("valid");
    assertTrue(a.isActivated());
    assertNull(a.getActivationToken());
    assertNull(a.getActivationExpiresAt());
    verify(repository).save(a);
  }

  // Kiểm tra token kích hoạt không tồn tại hoặc hết hạn bị từ chối.
  @Test
  @CaseIds("TC-031")
  void rejectMissingAndExpiredActivation() {
    assertThrows(IllegalArgumentException.class, () -> service.activate("wrong"));
    Account a = account("qa", false);
    a.setActivated(false);
    a.setActivationExpiresAt(LocalDateTime.now().minusMinutes(1));
    when(repository.findByActivationToken("expired")).thenReturn(Optional.of(a));
    assertThrows(IllegalArgumentException.class, () -> service.activate("expired"));
    assertFalse(a.isActivated());
    verify(repository, never()).save(any());
  }

  // Kiểm tra gửi lại kích hoạt tạo token thay thế token cũ.
  @Test
  @CaseIds("TC-032")
  void resendReplacesActivationToken() {
    Account a = account("qa", false);
    a.setActivated(false);
    a.setActivationToken("old");
    when(repository.findByEmailIgnoreCase(a.getEmail())).thenReturn(Optional.of(a));
    service.prepareActivation(a.getEmail());
    assertNotEquals("old", a.getActivationToken());
    assertTrue(a.getActivationExpiresAt().isAfter(LocalDateTime.now().plusHours(23)));
  }

  // Kiểm tra đổi mật khẩu đồng thời xóa token đặt lại mật khẩu.
  @Test
  @CaseIds("TC-037")
  void changePasswordClearsResetToken() {
    Account a = account("qa", false);
    a.setResetToken("old");
    service.changePassword(a, "Test@1234", "New@12345");
    assertTrue(new BCryptPasswordEncoder().matches("New@12345", a.getPassword()));
    assertNull(a.getResetToken());
    verify(repository).save(a);
  }

  // Kiểm tra mật khẩu cũ sai thì không lưu thay đổi.
  @Test
  @CaseIds("TC-038")
  void wrongOldPasswordDoesNotWrite() {
    Account a = account("qa", false);
    String hash = a.getPassword();
    assertThrows(
        IllegalArgumentException.class, () -> service.changePassword(a, "wrong", "New@12345"));
    assertEquals(hash, a.getPassword());
    verify(repository, never()).save(any());
  }

  // Kiểm tra token đặt lại mật khẩu hết hạn sau 30 phút.
  @Test
  @CaseIds("TC-039")
  void prepareResetExpiresInThirtyMinutes() {
    Account a = account("qa", false);
    when(repository.findByEmailIgnoreCase(a.getEmail())).thenReturn(Optional.of(a));
    service.prepareReset(a.getEmail());
    assertNotNull(a.getResetToken());
    assertTrue(a.getResetExpiresAt().isAfter(LocalDateTime.now().plusMinutes(29)));
    assertTrue(a.getResetExpiresAt().isBefore(LocalDateTime.now().plusMinutes(31)));
  }

  // Kiểm tra tài khoản không tồn tại hoặc chưa kích hoạt không được đặt lại mật khẩu.
  @Test
  @CaseIds("TC-040")
  void unknownOrInactiveAccountCannotReset() {
    assertNull(service.prepareReset("unknown@example.invalid"));
    Account a = account("qa", false);
    a.setActivated(false);
    when(repository.findByEmailIgnoreCase(a.getEmail())).thenReturn(Optional.of(a));
    assertNull(service.prepareReset(a.getEmail()));
    verify(repository, never()).save(any());
  }

  // Kiểm tra đặt lại mật khẩu đổi bản mã và xóa token đã dùng.
  @Test
  @CaseIds("TC-041")
  void resetConsumesTokenAndChangesHash() {
    Account a = account("qa", false);
    a.setResetToken("valid");
    a.setResetExpiresAt(LocalDateTime.now().plusMinutes(5));
    when(repository.findByResetToken("valid")).thenReturn(Optional.of(a));
    service.reset("valid", "Reset@123");
    assertNull(a.getResetToken());
    assertNull(a.getResetExpiresAt());
    assertTrue(new BCryptPasswordEncoder().matches("Reset@123", a.getPassword()));
  }

  // Kiểm tra token đặt lại mật khẩu sai hoặc hết hạn bị từ chối.
  @Test
  @CaseIds("TC-042")
  void rejectExpiredOrInvalidReset() {
    assertThrows(IllegalArgumentException.class, () -> service.reset("wrong", "Reset@123"));
    Account a = account("qa", false);
    String hash = a.getPassword();
    a.setResetExpiresAt(LocalDateTime.now().minusSeconds(1));
    when(repository.findByResetToken("expired")).thenReturn(Optional.of(a));
    assertThrows(IllegalArgumentException.class, () -> service.reset("expired", "Reset@123"));
    assertEquals(hash, a.getPassword());
    verify(repository, never()).save(any());
  }
}
