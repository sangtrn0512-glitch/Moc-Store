package org.example.asm_java5.service;

import java.util.List;
import java.util.Optional;
import org.example.asm_java5.entity.Product;

public interface ProductService {
  List<Product> findAll();

  Optional<Product> findById(Integer id);

  Product save(Product entity);

  void deleteById(Integer id);
}
