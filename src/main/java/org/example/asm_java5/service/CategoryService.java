package org.example.asm_java5.service;

import java.util.List;
import java.util.Optional;
import org.example.asm_java5.entity.Category;

public interface CategoryService {
  List<Category> findAll();

  Optional<Category> findById(Integer id);

  Category save(Category entity);

  void deleteById(Integer id);
}
