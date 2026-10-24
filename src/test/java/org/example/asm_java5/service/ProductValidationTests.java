package org.example.asm_java5.service;

import static org.example.asm_java5.support.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import org.example.asm_java5.entity.Product;
import org.example.asm_java5.repository.*;
import org.example.asm_java5.support.CaseIds;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

class ProductValidationTests {
  ProductRepository repository;
  ProductService service;
  Product p;

  // Tạo Repository giả, ProductService và sản phẩm mẫu trước mỗi test.
  @BeforeEach
  void setup() {
    repository = mock(ProductRepository.class);
    service = new ProductService(repository, mock(OrderDetailRepository.class));
    p = product(1, "100000", "10");
  }

  // Kiểm tra giá âm, quá lớn hoặc quá số chữ số thập phân bị từ chối.
  @ParameterizedTest
  @ValueSource(strings = {"-0.01", "100000000000000000.00", "1.001"})
  @CaseIds("TC-081")
  void rejectInvalidPrice(String price) {
    p.setPrice(new BigDecimal(price));
    assertThrows(IllegalArgumentException.class, () -> service.save(p));
    verify(repository, never()).save(any());
  }

  // Kiểm tra chấp nhận giá ở hai giới hạn hợp lệ.
  @ParameterizedTest
  @ValueSource(strings = {"0", "99999999999999999.99"})
  @CaseIds("TC-081")
  void acceptPriceBoundaries(String price) {
    p.setPrice(new BigDecimal(price));
    service.save(p);
    verify(repository).save(p);
  }

  // Kiểm tra giảm giá ngoài giới hạn hoặc quá số chữ số thập phân bị từ chối.
  @ParameterizedTest
  @ValueSource(strings = {"-0.01", "100.01", "1.001"})
  @CaseIds("TC-082")
  void rejectInvalidDiscount(String discount) {
    p.setDiscount(new BigDecimal(discount));
    assertThrows(IllegalArgumentException.class, () -> service.save(p));
    verify(repository, never()).save(any());
  }

  // Kiểm tra chấp nhận giảm giá 0% và 100%.
  @ParameterizedTest
  @ValueSource(strings = {"0", "100"})
  @CaseIds("TC-082")
  void acceptDiscountBoundaries(String discount) {
    p.setDiscount(new BigDecimal(discount));
    service.save(p);
    verify(repository).save(p);
  }

  // Kiểm tra giới hạn độ dài tên sản phẩm.
  @Test
  @CaseIds("TC-080")
  void nameBoundaries() {
    p.setName(" ");
    assertThrows(IllegalArgumentException.class, () -> service.save(p));
    p.setName("a".repeat(201));
    assertThrows(IllegalArgumentException.class, () -> service.save(p));
    p.setName("a".repeat(200));
    service.save(p);
    verify(repository, times(1)).save(p);
  }

  // Kiểm tra sản phẩm bắt buộc có danh mục.
  @Test
  @CaseIds("TC-083")
  void categoryRequired() {
    p.setCategory(null);
    assertThrows(IllegalArgumentException.class, () -> service.save(p));
    verify(repository, never()).save(any());
  }

  // Kiểm tra từ chối đường dẫn ảnh không hợp lệ.
  @ParameterizedTest
  @ValueSource(strings = {"../x.png", "a/b.png", "a\\b.png"})
  @CaseIds("TC-084")
  void rejectImagePaths(String image) {
    p.setImage(image);
    assertThrows(IllegalArgumentException.class, () -> service.save(p));
    verify(repository, never()).save(any());
  }
}
