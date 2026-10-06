package org.example.asm_java5.service;

import java.util.List;
import java.util.Optional;
import org.example.asm_java5.entity.Account;

public interface AccountService {
  List<Account> findAll();

  Optional<Account> findById(String id);

  Account save(Account entity);

  void deleteById(String id);
}
