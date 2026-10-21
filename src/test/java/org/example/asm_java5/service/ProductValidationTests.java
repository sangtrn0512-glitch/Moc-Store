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

  @BeforeEach
  void setup() {
    repository = mock(ProductRepository.class);
    service = new ProductService(repository, mock(OrderDetailRepository.class));
    p = product(1, "100000", "10");
  }

  @ParameterizedTest
  @ValueSource(strings = {"-0.01", "100000000000000000.00", "1.001"})
  @CaseIds("TC-081")
  void rejectInvalidPrice(String price) {
    p.setPrice(new BigDecimal(price));
    assertThrows(IllegalArgumentException.class, () -> service.save(p));
    verify(repository, never()).save(any());
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "99999999999999999.99"})
  @CaseIds("TC-081")
  void acceptPriceBoundaries(String price) {
    p.setPrice(new BigDecimal(price));
    service.save(p);
    verify(repository).save(p);
  }

  @ParameterizedTest
  @ValueSource(strings = {"-0.01", "100.01", "1.001"})
  @CaseIds("TC-082")
  void rejectInvalidDiscount(String discount) {
    p.setDiscount(new BigDecimal(discount));
    assertThrows(IllegalArgumentException.class, () -> service.save(p));
    verify(repository, never()).save(any());
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "100"})
  @CaseIds("TC-082")
  void acceptDiscountBoundaries(String discount) {
    p.setDiscount(new BigDecimal(discount));
    service.save(p);
    verify(repository).save(p);
  }

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

  @Test
  @CaseIds("TC-083")
  void categoryRequired() {
    p.setCategory(null);
    assertThrows(IllegalArgumentException.class, () -> service.save(p));
    verify(repository, never()).save(any());
  }

  @ParameterizedTest
  @ValueSource(strings = {"../x.png", "a/b.png", "a\\b.png"})
  @CaseIds("TC-084")
  void rejectImagePaths(String image) {
    p.setImage(image);
    assertThrows(IllegalArgumentException.class, () -> service.save(p));
    verify(repository, never()).save(any());
  }
}
