package org.example.asm_java5.repository;

import java.util.Optional;
import org.example.asm_java5.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
  Optional<Account> findByEmailIgnoreCase(String email);

  Optional<Account> findByResetToken(String token);

  boolean existsByEmailIgnoreCaseAndUsernameNot(String email, String username);

  org.springframework.data.domain.Page<Account>
      findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(
          String username, String name, org.springframework.data.domain.Pageable pageable);

  boolean existsByEmail(String email);

  Optional<Account> findByActivationToken(String token);
}
