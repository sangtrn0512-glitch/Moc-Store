package org.example.asm_java5.repository;

import org.example.asm_java5.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
  boolean existsByName(String name);

  boolean existsByNameAndIdNot(String name, Integer id);
}
