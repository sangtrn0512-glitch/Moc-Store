package org.example.asm_java5.service;

import java.util.List;
import java.util.Optional;
import org.example.asm_java5.entity.OrderDetail;

public interface OrderDetailService {
  List<OrderDetail> findAll();

  Optional<OrderDetail> findById(Long id);

  OrderDetail save(OrderDetail entity);

  void deleteById(Long id);
}
