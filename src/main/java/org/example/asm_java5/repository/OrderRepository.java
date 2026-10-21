package org.example.asm_java5.repository;

import java.util.List;
import java.util.Optional;
import org.example.asm_java5.entity.Order;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

public interface OrderRepository extends JpaRepository<Order, Long> {
  @Override
  @EntityGraph(attributePaths = "account")
  List<Order> findAll();

  @Override
  @EntityGraph(attributePaths = "account")
  Optional<Order> findById(Long id);

  @EntityGraph(attributePaths = "account")
  List<Order> findByAccount_UsernameOrderByIdDesc(String username);

  boolean existsByAccount_Username(String username);

  @EntityGraph(attributePaths = "account")
  Page<Order> findByAccount_UsernameContainingIgnoreCase(String username, Pageable pageable);
}
