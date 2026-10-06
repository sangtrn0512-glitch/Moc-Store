package org.example.asm_java5.service;

public interface CartService {
  void add(Integer productId);

  void update(Integer productId, int quantity);

  void remove(Integer productId);

  void clear();
}
