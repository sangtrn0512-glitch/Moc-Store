package org.example.asm_java5.service;

import java.util.List;
import java.util.Optional;
import org.example.asm_java5.entity.Category;
import org.example.asm_java5.repository.CategoryRepository;
import org.example.asm_java5.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;

  public CategoryService(
      CategoryRepository categoryRepository, ProductRepository productRepository) {
    this.categoryRepository = categoryRepository;
    this.productRepository = productRepository;
  }

  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  public Optional<Category> findById(Integer id) {
    return categoryRepository.findById(id);
  }

  public Category save(Category entity) {
    if (entity.getName() == null || entity.getName().isBlank() || entity.getName().length() > 100) {
      throw new IllegalArgumentException("Tên loại hàng cần từ 1 đến 100 ký tự");
    }
    return categoryRepository.save(entity);
  }

  public void deleteById(Integer id) {
    if (productRepository.existsByCategory_Id(id)) {
      throw new IllegalStateException("Không thể xóa loại hàng đang có sản phẩm");
    }

    categoryRepository.deleteById(id);
  }

  public boolean existsByName(String name) {
    return categoryRepository.existsByName(name);
  }

  public boolean existsByNameAndIdNot(String name, Integer id) {
    return categoryRepository.existsByNameAndIdNot(name, id);
  }
}
