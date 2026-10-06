package org.example.asm_java5.repository;

import org.example.asm_java5.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {}
