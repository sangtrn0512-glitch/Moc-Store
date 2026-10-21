package org.example.asm_java5.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.example.asm_java5.entity.Account;
import org.example.asm_java5.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
  private final AccountRepository accountRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public List<Account> findAll() {
    return accountRepository.findAll();
  }

  public Optional<Account> findById(String id) {
    return accountRepository.findById(id);
  }

  public Account save(Account entity) {
    validate(entity);
    return accountRepository.save(entity);
  }

  public void deleteById(String id) {
    accountRepository.deleteById(id);
  }

  public boolean existsByUsername(String username) {
    return accountRepository.existsById(username);
  }

  public boolean existsByEmail(String email) {
    return accountRepository.existsByEmail(email);
  }

  public Account register(Account account) {
    validate(account);
    checkPassword(account.getPassword());
    String hashedPassword = passwordEncoder.encode(account.getPassword());

    account.setPassword(hashedPassword);
    account.setActivated(false);
    account.setAdmin(false);
    account.setActivationToken(UUID.randomUUID().toString());
    account.setActivationExpiresAt(LocalDateTime.now().plusHours(24));
    return accountRepository.save(account);
  }

  public void activate(String token) {
    if (token == null || token.isBlank()) {
      throw new IllegalArgumentException("Mã kích hoạt không hợp lệ");
    }

    Account account = accountRepository.findByActivationToken(token).orElse(null);

    if (account == null) {
      throw new IllegalArgumentException("Mã kích hoạt không hợp lệ");
    }

    if (account.getActivationExpiresAt() == null
        || !account.getActivationExpiresAt().isAfter(LocalDateTime.now())) {
      throw new IllegalArgumentException("Mã kích hoạt đã hết hạn");
    }

    account.setActivated(true);
    account.setActivationToken(null);
    account.setActivationExpiresAt(null);

    accountRepository.save(account);
  }

  public void checkPassword(String password) {
    if (password == null
        || password.isBlank()
        || password.length() < 8
        || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72)
      throw new IllegalArgumentException("Mật khẩu cần ít nhất 8 ký tự, tối đa 72 byte");
  }

  public void validate(Account account) {
    if (account.getUsername() == null
        || account.getUsername().isBlank()
        || account.getUsername().length() > 50)
      throw new IllegalArgumentException("Tên đăng nhập không hợp lệ");
    if (account.getFullName() == null
        || account.getFullName().isBlank()
        || account.getFullName().length() > 100)
      throw new IllegalArgumentException("Họ tên không hợp lệ");
    if (account.getEmail() == null
        || account.getEmail().length() > 255
        || !account.getEmail().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))
      throw new IllegalArgumentException("Email không hợp lệ");
    if (accountRepository.existsByEmailIgnoreCaseAndUsernameNot(
        account.getEmail(), account.getUsername()))
      throw new IllegalArgumentException("Email đã được sử dụng");
  }

  public void changePassword(Account account, String oldPassword, String newPassword) {
    if (!passwordEncoder.matches(oldPassword, account.getPassword()))
      throw new IllegalArgumentException("Mật khẩu hiện tại không đúng");
    setPassword(account, newPassword);
  }

  public void setPassword(Account account, String password) {
    checkPassword(password);
    account.setPassword(passwordEncoder.encode(password));
    account.setResetToken(null);
    account.setResetExpiresAt(null);
    accountRepository.save(account);
  }

  public Account prepareReset(String email) {
    Account account = accountRepository.findByEmailIgnoreCase(email.trim()).orElse(null);
    if (account == null || !account.isActivated()) return null;
    account.setResetToken(UUID.randomUUID().toString());
    account.setResetExpiresAt(LocalDateTime.now().plusMinutes(30));
    return accountRepository.save(account);
  }

  @org.springframework.transaction.annotation.Transactional
  public void reset(String token, String password) {
    if (token == null || token.isBlank()) throw new IllegalArgumentException("Link không hợp lệ");
    Account account = accountRepository.findByResetToken(token).orElse(null);
    if (account == null
        || account.getResetExpiresAt() == null
        || !account.getResetExpiresAt().isAfter(LocalDateTime.now()))
      throw new IllegalArgumentException("Link không hợp lệ hoặc hết hạn");
    setPassword(account, password);
  }

  public Account prepareActivation(String email) {
    Account account = accountRepository.findByEmailIgnoreCase(email.trim()).orElse(null);
    if (account == null || account.isActivated() || account.getActivationToken() == null)
      return null;
    account.setActivationToken(UUID.randomUUID().toString());
    account.setActivationExpiresAt(LocalDateTime.now().plusHours(24));
    return accountRepository.save(account);
  }
}
