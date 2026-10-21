package org.example.asm_java5.service;

import java.util.*;
import org.example.asm_java5.entity.OrderDetail;
import org.example.asm_java5.repository.OrderDetailRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailService {
  private final OrderDetailRepository repository;

  public OrderDetailService(OrderDetailRepository repository) {
    this.repository = repository;
  }

  public List<OrderDetail> findAll() {
    return repository.findAll();
  }

  public Optional<OrderDetail> findById(Long id) {
    return repository.findById(id);
  }

  public OrderDetail save(OrderDetail detail) {
    return repository.save(detail);
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }
}
