package org.example.asm_java5.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.example.asm_java5.entity.Product;
import org.example.asm_java5.repository.OrderDetailRepository;
import org.example.asm_java5.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
  private final ProductRepository productRepository;

  private final OrderDetailRepository orderDetailRepository;

  public ProductService(
      ProductRepository productRepository, OrderDetailRepository orderDetailRepository) {
    this.orderDetailRepository = orderDetailRepository;
    this.productRepository = productRepository;
  }

  public List<Product> findAll() {
    return productRepository.findAll();
  }

  public Optional<Product> findById(Integer id) {
    return productRepository.findById(id);
  }

  public Product save(Product product) {
    if (product.getName() == null || product.getName().isBlank()) {
      throw new IllegalArgumentException("Vui lòng nhập tên sản phẩm");
    }
    if (product.getName().length() > 200) {
      throw new IllegalArgumentException("Tên sản phẩm không được vượt quá 200 ký tự");
    }
    if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn hoặc bằng 0");
    }
    if (product.getPrice().compareTo(new BigDecimal("99999999999999999.99")) > 0
        || product.getPrice().scale() > 2) {
      throw new IllegalArgumentException("Giá quá lớn hoặc có quá 2 chữ số thập phân");
    }
    if (product.getDiscount() == null
        || product.getDiscount().compareTo(BigDecimal.ZERO) < 0
        || product.getDiscount().compareTo(new BigDecimal("100")) > 0) {
      throw new IllegalArgumentException("Giảm giá phải từ 0 đến 100 phần trăm");
    }
    if (product.getDiscount().scale() > 2) {
      throw new IllegalArgumentException("Giảm giá chỉ được có tối đa 2 chữ số thập phân");
    }
    String image = product.getImage();
    if (image != null
        && (image.length() > 255
            || image.contains("/")
            || image.contains("\\")
            || image.contains(".."))) {
      throw new IllegalArgumentException("Chỉ nhập tên file ảnh, không nhập đường dẫn");
    }
    if (product.getCategory() == null) {
      throw new IllegalArgumentException("Vui lòng chọn loại hàng");
    }
    return productRepository.save(product);
  }

  public void deleteById(Integer id) {
    if (!productRepository.existsById(id))
      throw new IllegalStateException("Không tìm thấy sản phẩm");
    if (orderDetailRepository.existsByProduct_Id(id))
      throw new IllegalStateException("Sản phẩm đã có trong đơn hàng. Hãy chuyển sang Ngừng bán.");
    productRepository.deleteById(id);
  }

  public List<Product> findRelatedProducts(Integer categoryId, Integer productId) {

    return productRepository.findByCategory_IdAndIdNot(categoryId, productId);
  }

  public List<Product> findByCategoryId(Integer categoryId) {
    return productRepository.findByCategory_Id(categoryId);
  }

  public List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {

    return productRepository.findByPriceBetween(minPrice, maxPrice);
  }

  public List<Product> findNewProducts() {
    return productRepository.findTop4ByAvailableTrueOrderByCreateDateDescIdDesc();
  }

  public List<Product> findDiscountProducts() {
    return productRepository.findTop4ByAvailableTrueAndDiscountGreaterThanOrderByDiscountDesc(
        BigDecimal.ZERO);
  }

  public List<Product> findBestSellingProducts() {
    return productRepository.findBestSellingProducts();
  }
}
