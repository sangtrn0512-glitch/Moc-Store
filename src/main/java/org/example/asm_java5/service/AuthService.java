package org.example.asm_java5.service;

public interface AuthService {
  boolean login(String username, String password);

  void logout();
}
