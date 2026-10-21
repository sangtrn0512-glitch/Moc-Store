package org.example.asm_java5.repository;

import java.util.List;
import org.example.asm_java5.entity.OrderDetail;
import org.springframework.data.jpa.repository.*;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
  boolean existsByProduct_Id(Integer productId);

  @EntityGraph(attributePaths = {"product", "product.category", "order", "order.account"})
  List<OrderDetail> findByOrder_Id(Long id);

  @EntityGraph(attributePaths = {"product", "product.category", "order", "order.account"})
  List<OrderDetail> findByOrder_Account_UsernameAndOrder_Status(String username, String status);

  @EntityGraph(attributePaths = {"product", "product.category", "order", "order.account"})
  List<OrderDetail> findByOrder_Status(String status);
}
