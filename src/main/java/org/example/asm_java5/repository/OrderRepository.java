package org.example.asm_java5.repository;

import org.example.asm_java5.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
