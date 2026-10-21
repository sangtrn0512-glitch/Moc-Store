package org.example.asm_java5.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.example.asm_java5.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {
  @Override
  @EntityGraph(attributePaths = "category")
  Optional<Product> findById(Integer id);

  boolean existsByCategory_Id(Integer categoryId);

  @Override
  @EntityGraph(attributePaths = "category")
  List<Product> findAll();

  List<Product> findByCategory_IdAndIdNot(Integer categoryId, Integer productId);

  @EntityGraph(attributePaths = "category")
  List<Product> findByCategory_Id(Integer categoryId);

  @EntityGraph(attributePaths = "category")
  List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

  @EntityGraph(attributePaths = "category")
  List<Product> findTop4ByAvailableTrueOrderByCreateDateDescIdDesc();

  @EntityGraph(attributePaths = "category")
  List<Product> findTop4ByAvailableTrueAndDiscountGreaterThanOrderByDiscountDesc(
      BigDecimal discount);

  @Query(
      value =
          """
          SELECT TOP 4 p.*
          FROM products p
          JOIN (
              SELECT d.product_id, SUM(d.quantity) AS total_quantity
              FROM order_details d
              JOIN orders o ON o.id = d.order_id
              WHERE o.status = 'COMPLETED'
              GROUP BY d.product_id
          ) sales ON sales.product_id = p.id
          WHERE p.available = 1
          ORDER BY sales.total_quantity DESC, p.id DESC
          """,
      nativeQuery = true)
  List<Product> findBestSellingProducts();
}
