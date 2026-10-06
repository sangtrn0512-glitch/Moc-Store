package org.example.asm_java5.service;

import java.util.List;
import java.util.Optional;
import org.example.asm_java5.entity.Order;

public interface OrderService {
  List<Order> findAll();

  Optional<Order> findById(Long id);

  Order save(Order entity);

  void deleteById(Long id);
}
